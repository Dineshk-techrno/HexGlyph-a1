# HexGlyph — Native Android Secure Visual Cipher + Stealth Glyph

> v1.10 · Kotlin + Jetpack Compose · Android 8.0+ (API 26–34)

HexGlyph encodes end-to-end encrypted messages into **hexagonal dot-pattern glyphs** that look like abstract art, and also supports an **invisible Stealth Glyph transport** layer that hides encrypted payloads inside ordinary-looking images via LSB steganography. Fully offline, no server, no cloud.

---

## Architecture Overview

```
Plaintext
 → CryptoEngine.encodeFull()
 → Encrypted Output Block
 → Optional Group Protection (AES-GCM outer layer)
 → [Visible Path]  GlyphRenderer  → HexGlyph Bitmap (1440×1440 px)
 → [Stealth Path]  StealthEncoder → Ordinary-looking Carrier Image
```

---

## Project Structure

```
hexglyph-android/
├── app/src/main/java/com/hexglyph/
│   ├── HexGlyphApp.kt                    # @HiltAndroidApp
│   ├── di/
│   │   └── AppModule.kt                  # Hilt singleton bindings
│   ├── crypto/
│   │   ├── CryptoEngine.kt               # encodeFull / decodeFull — main pipeline
│   │   ├── KeyDerivation.kt              # HKDF-SHA-256, epoch, Fisher-Yates
│   │   ├── ChaCha20.kt                   # Pure-Kotlin ChaCha20 stream cipher
│   │   ├── Obfuscator.kt                 # 3-round XOR+rotate obfuscation
│   │   ├── ReedSolomon.kt                # GF(2^8) RS encode/decode
│   │   └── KeystoreManager.kt            # Android Keystore + EncryptedSharedPrefs
│   ├── renderer/
│   │   ├── HexMath.kt                    # Axial grid, spiral enumeration, geometry
│   │   ├── GlyphRenderer.kt              # Bitmap renderer (1440×1440 px)
│   │   └── GlyphExporter.kt              # PNG export, FileProvider share
│   ├── scanner/
│   │   ├── ScanEngine.kt                 # CameraX orchestration (visible glyphs)
│   │   ├── BlobDetector.kt               # Orange anchor blob detection
│   │   └── GridSampler.kt                # Affine transform + bit sampling
│   ├── steganography/
│   │   ├── StealthEncoder.kt             # Embed encrypted payload into carrier image
│   │   ├── StealthDecoder.kt             # Extract & decrypt hidden payload
│   │   ├── StealthScanEngine.kt          # Detection + decode entry point
│   │   ├── SyntheticCarrierGenerator.kt  # Procedural carrier image fallback
│   │   ├── CarrierSelector.kt            # User / pool / synthetic carrier selection
│   │   ├── RegionAnalyzer.kt             # Sobel-based texture mask for safe embedding
│   │   ├── RandomPixelMapper.kt          # Seeded Fisher-Yates slot sequence
│   │   ├── BitStreamWriter.kt            # LSB bit writer into bitmap pixels
│   │   ├── BitStreamReader.kt            # LSB bit reader from bitmap pixels
│   │   └── SignatureDetector.kt          # HXGS magic header embed / detect
│   ├── database/
│   │   └── GlyphDatabase.kt              # Room DB: GlyphEntity + GlyphDao
│   ├── ui/
│   │   ├── MainActivity.kt               # Navigation scaffold (4-tab bottom nav)
│   │   ├── theme/HexGlyphTheme.kt        # Material3 dark theme
│   │   ├── components/
│   │   │   └── GlyphCanvas.kt            # Compose hex canvas component
│   │   └── screens/
│   │       ├── EncodeScreen.kt           # Visible Glyph encode UI
│   │       ├── DecodeScreen.kt           # Visible Glyph decode/scan UI
│   │       ├── StealthEncodeScreen.kt    # Stealth Glyph encode UI
│   │       └── StealthDecodeScreen.kt    # Stealth Glyph detect/decode UI
│   ├── viewmodel/
│   │   ├── EncodeViewModel.kt            # Visible encode state + business logic
│   │   ├── DecodeViewModel.kt            # Visible decode state + business logic
│   │   └── StealthViewModel.kt           # Stealth encode/decode/inspect logic
│   └── utils/
│       └── Utils.kt                      # RootDetector + SecureClipboard
```

---

## Core Technologies

- AES-GCM-256 (Android Keystore hardware-backed)
- ChaCha20 (pure-Kotlin stream cipher)
- AES-CTR-256
- HKDF-SHA-256 with daily epoch key rotation
- Reed-Solomon ECC (GF(2^8), 10 symbols per 255-byte block)
- Fisher-Yates shuffle (seeded from key material)
- Randomised LSB Steganography with Sobel region-aware embedding
- HXGS hidden signature system for payload detection

---

## Cryptographic Pipeline

### Encode
| Step | Operation | Detail |
|------|-----------|--------|
| 1 | Derive K1, K2 | HKDF-SHA-256: ikm=groupCode+epochDay, salt=gcmIv |
| 2 | Assemble plainBlock | MAGIC(4) + VERSION(1) + timestamp(8) + gcmIv(12) + ptLen(2) + plaintext |
| 3 | AES-GCM-256 | Key=K1, IV=gcmIv via Android Keystore |
| 4 | ChaCha20 XOR | Key=K2, nonce=chachaNonce |
| 5 | AES-CTR-256 XOR | Key=K1, counter=ctrNonce |
| 6 | Fisher-Yates shuffle | Seeded from SHA-256(K1) |
| 7 | Assemble outputBlock | gcmIv + chachaNonce + ctrNonce + shuffled + authTag |
| 8 | Reed-Solomon encode | 10 ECC symbols per 255-byte block |
| 9 | Obfuscation | 3-round XOR+rotate using SHA-256(K1) round keys |

Decode is the exact reverse. **Any step failure throws — no partial data returned.**

---

## Stealth Glyph Transport

### Stealth Payload Wire Format (big-endian)
```
[0..3]   magic        = 0x48 0x58 0x47 0x53  ("HXGS")
[4]      version      = 0x01
[5]      flags        = bit-0: groupProtected
[6..9]   payloadLen   = uint32 — byte count of encryptedBlock
[10..13] checksum     = CRC32 of encryptedBlock (uint32)
[14..]   encryptedBlock (305 bytes from CryptoEngine.encodeFull)
```
Total header overhead: 14 bytes → minimum payload: **319 bytes**.

### Embedding Strategy
1. Sobel edge detection (`RegionAnalyzer`) builds a texture mask
2. High-texture pixels (edges, grain, shadows) are preferred for embedding
3. Pixel order is randomised using a seeded Fisher-Yates shuffle (`RandomPixelMapper`)
4. Only the LSB of each R/G/B channel is modified — visually imperceptible (±1 intensity unit)
5. HXGS signature is embedded at a canonical well-known slot sequence for fast detection

### Carrier Image Priority
1. User-supplied URI (photo picker)
2. Asset pool (`assets/carriers/` — 80+ themed dark/metallic/urban images)
3. Synthetic fallback (`SyntheticCarrierGenerator`) — procedural dark aesthetic images

---

## Glyph Format (Visible Path)

| Parameter | Value |
|-----------|-------|
| Grid Radius | 28 rings |
| Total Hex Cells | 2,437 |
| Data Cells | 2,434 |
| Data Capacity | 304 bytes |
| Max Plaintext | **197 bytes** |
| ECC Symbols | 10 per 255-byte block |
| Output Bitmap | ~1,440 × 1,440 px |

### Colour Palette
| Element | Hex | Purpose |
|---------|-----|---------|
| Anchor cells | `#FF6B35` | Orange — orientation markers |
| Lit cell (bit=1) | `#F0F0F0` | Near-white |
| Dark cell (bit=0) | `#1A1A2E` | Near-black |
| Background | `#0F0F1A` | Canvas |

---

## Build

**Prerequisites:** Android Studio Hedgehog+, JDK 17, SDK API 34

```bash
git clone https://github.com/your-org/hexglyph-android.git
cd hexglyph-android
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK (minified + ProGuard)
./gradlew bundleRelease      # AAB for Play Store
```

---

## Current Status & Known Limitations

### Implemented ✅
- Visible Glyph encode/decode pipeline (full)
- Stealth transport architecture (full implementation)
- HXGS hidden signature system
- Randomised pixel mapping with region-aware embedding
- Group protection pipeline (AES-GCM outer layer)
- Carrier image system (user / pool / synthetic)
- Full Compose UI with 4-tab navigation

### Pending / Incomplete ⚠️
- Region-aware adaptive embedding optimisation (Sobel threshold tuning)
- Anti-forensic hardening (histogram normalisation, RS-aware embedding)
- ECC transport layer for stealth payload (currently relies on CRC-32 only)
- Full Android runtime testing across device matrix
- Multi-device / multi-API-level testing
- Production hardening (ProGuard rules for steganography classes)

---

## Security Notes

- **Offline-first** — no server, no cloud, keys never leave the device
- **Android Keystore** — hardware-backed key storage (secure enclave)
- **Key rotation** — daily at 00:00 UTC; glyphs from yesterday cannot be decoded
- **Stealth images** — export as PNG only; JPEG/social compression destroys hidden payload
- **No forward secrecy** — past glyphs decryptable if Group Code is known
- **No revocation** — once distributed, a glyph cannot be un-shared
- **Root detection** — warning only; cannot fully protect a rooted device

---

*HexGlyph v1.10 · Confidential — HexGlyph Proprietary*
