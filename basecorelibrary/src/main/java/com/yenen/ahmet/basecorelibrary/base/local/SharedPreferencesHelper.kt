package com.yenen.ahmet.basecorelibrary.base.local

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.lang.Exception

class SharedPreferencesHelper constructor(context: Context) {

    private val sharedPref: SharedPreferences


    init {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        val masterKey =MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        sharedPref = try{
            EncryptedSharedPreferences.create(
                context,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }catch (ex:Exception){
            context.getSharedPreferences("secret_shared_prefs", Context.MODE_PRIVATE)
        }


    }

    fun addValueApply(keyValue: String, stringValue: String) {
        val editor = sharedPref.edit()
        editor.putString(keyValue, stringValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, intValue: Int) {
        val editor = sharedPref.edit()
        editor.putInt(keyValue, intValue)
        editor.apply()
    }

    fun addValueApply(keyValue: String, floatValue: Float) {
        val editor = sharedPref.edit()
        editor.putFloat(keyValue, floatValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, booleanValue: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(keyValue, booleanValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, longValue: Long) {
        val editor = sharedPref.edit()
        editor.putLong(keyValue, longValue)
        editor.apply()
    }

    fun addValueApply(keyValue: String, setStringValue: Set<String>) {
        val editor = sharedPref.edit()
        editor.putStringSet(keyValue, setStringValue)
        editor.apply()
    }


    fun getValue(keyValue: String, defaultValue: String): String {
        return sharedPref.getString(keyValue, defaultValue)!!
    }

    fun getValue(keyValue: String, defaultValue: Int): Int {
        return sharedPref.getInt(keyValue, defaultValue)
    }


    fun getValue(keyValue: String, defaultValue: Long): Long {
        return sharedPref.getLong(keyValue, defaultValue)
    }

    fun getValue(keyValue: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(keyValue, defaultValue)
    }

    fun getValue(keyValue: String, defaultValue: Float): Float {
        return sharedPref.getFloat(keyValue, defaultValue)
    }

    fun getValue(keyValue: String, defaultValue: Set<String>): Set<String> {
        return sharedPref.getStringSet(keyValue, defaultValue)!!
    }


    fun clearPreferences() {
        sharedPref.edit().let {
            it.clear()
            it.commit()
        }
    }

    fun removeKey(keyValue: String) {
        sharedPref.edit().let {
            it.remove(keyValue)
            it.apply()
        }
    }


    fun getFirebaseToken(): String {
        return getValue("FIREBASE_TOKEN", "")
    }

    fun addFireBaseToken(token: String) {
        addValueApply("FIREBASE_TOKEN", token)
    }

    fun getNamePassword(): String {
        return getValue("PASSWORD", "")
    }

    fun addNamePassword(password: String) {
        addValueApply("PASSWORD", password)
    }

    fun getUserName(): String {
        return getValue("USER_NAME", "")
    }

    fun addUserName(userName: String) {
        addValueApply("USER_NAME", userName)
    }

    fun getUserId(): String {
        return getValue("USER_ID", "")
    }

    fun addUserId(userId: String) {
        addValueApply("USER_ID", userId)
    }

    fun getAppIsStart(): Boolean {
        return getValue("IS_START", false)
    }

    fun addAppIsStart(isStart: Boolean) {
        addValueApply("IS_START", isStart)
    }

    fun getToken(): String {
        return getValue("TOKEN", "")
    }

    fun addToken(token: String) {
        addValueApply("TOKEN", token)
    }

    fun getLanguage(): String {
        return getValue("LANGUAGE", "")
    }

    fun addLanguage(language: String) {
        addValueApply("LANGUAGE", language)
    }


    fun getDeviceId(): String {
        return getValue("DEVICE_ID", "")
    }

    fun addDeviceId(deviceId: String) {
        addValueApply("DEVICE_ID", deviceId)
    }

    fun getProfileImgUrl(): String {
        return getValue("PROFILE_IMG_URL", "")
    }

    fun addProfileImgUrl(url: String) {
        addValueApply("PROFILE_IMG_URL", url)
    }

    fun getLoginType(): String {
        return getValue("LOGIN_TYPE", "")
    }

    fun addLoginType(type: String) {
        addValueApply("LOGIN_TYPE", type)
    }

    fun getBirthDay(): String {
        return getValue("BIRTH_DAY", "")
    }

    fun addBirthDay(birthDay: String) {
        addValueApply("BIRTH_DAY", birthDay)
    }


}