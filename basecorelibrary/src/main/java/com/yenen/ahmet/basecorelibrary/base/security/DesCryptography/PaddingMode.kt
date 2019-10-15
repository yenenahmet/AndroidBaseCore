package com.mhamdaoui.nfcsmartcard.security.descrypto

enum class PaddingMode(val paddingMode: String) {
    NO_PADDING("NoPadding"),
    PKCS5_PADDING("PKCS5Padding")
}