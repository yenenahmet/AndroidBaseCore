package com.yenen.ahmet.basecorelibrary.base.security.descryptography


import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DesAlgorithm {

    @Throws(Exception::class)
    fun getTripleDes(input: ByteArray,key1:ByteArray,key2:ByteArray): ByteArray {
        val algo = getAlgo(Algorithm.DES,ChipperMode.ECB,PaddingMode.NO_PADDING)
        val a1 = encrypt(input, key1, algo)
        val b1 = decrypt(a1, key2, algo)
        return encrypt(b1, key1, algo)
    }

    @Throws(Exception::class)
    private fun cryptoData(algo: String, key: SecretKey, encryptMode: Int, bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algo)
        if (algo.contains("ECB")) {
            cipher.init(encryptMode, key)
        } else {
            val paramSpec = IvParameterSpec(Constants.iv)
            cipher.init(encryptMode, key, paramSpec)
        }
        return cipher.doFinal(bytes)
    }

    @Throws(Exception::class)
    fun encrypt(data: ByteArray, key: ByteArray, algo: String): ByteArray {
        val keys = SecretKeySpec(key, algo)
        return cryptoData(algo, keys, Cipher.ENCRYPT_MODE, data)
    }

    @Throws(Exception::class)
    fun decrypt(data: ByteArray, key: ByteArray, algo: String): ByteArray {
        val keys = SecretKeySpec(key, algo)
        return cryptoData(algo, keys, Cipher.DECRYPT_MODE, data)
    }

    fun getAlgo(algorithm:Algorithm,chipperMode:ChipperMode,paddingMode: PaddingMode): String {
        return "${algorithm.algorithm}/${chipperMode.mode}/${paddingMode.paddingMode}"
    }
}