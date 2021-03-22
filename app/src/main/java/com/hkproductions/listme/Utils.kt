package com.hkproductions.listme

import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.host.database.HostData

fun createBitmap(text: String): Bitmap {
    val width = 400

    val writer = QRCodeWriter()
    var bitmap: Bitmap? = null
    val bitMatrix: BitMatrix?

    try {
        bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, width)
        bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        for (i in 0 until width) {
            for (j in 0 until width) {
                bitmap.setPixel(i, j, if (bitMatrix.get(i, j)) -16777216 else -1)
            }
        }
    } catch (e: WriterException) {
        e.printStackTrace()
    }
    return bitmap!!
}

/**
 * make an Contact (GuestData) into CSV
 * @param contact GuestData that convert
 */
fun contactToText(contact: GuestData): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(contact.firstName + ";")
    stringBuilder.append(contact.lastName + ";")
    stringBuilder.append(contact.street + ";")
    stringBuilder.append(contact.houseNumber + ";")
    stringBuilder.append(contact.postalCode + ";")
    stringBuilder.append(contact.city + ";")
    stringBuilder.append(contact.phoneNumber + ";")
    return stringBuilder.toString()
}

/**
 * make an Contact into CSV
 * Shorter Version to safe place
 *
 */
private fun contactToShorterText(contact: GuestData): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(contact.firstName + ";")
    stringBuilder.append(contact.lastName + ";")
    stringBuilder.append(";")//street
    stringBuilder.append(";")//houseNumber
    stringBuilder.append(";")//postal code
    stringBuilder.append(";")//city
    stringBuilder.append(contact.phoneNumber + ";")
    return stringBuilder.toString()
}

/**
 * make an List of Contacts into CSV
 * @param contacts Lsit of GuestData that convert
 */
fun contactListToText(contacts: List<GuestData>): String {
    val stringBuilder = StringBuilder()

    for (contact in contacts) {
        stringBuilder.append(contactToText(contact))
    }
    return stringBuilder.toString()
}

/**
 * get an string and convert into GuestData
 * @param text CSV String convert into GuestData
 */
fun textToContact(text: String): GuestData {
    val stringList: List<String> = text.split(";").dropLast(1)
    val data = GuestData()
    Log.i("Listme", "${stringList.size} $stringList")
    data.apply {
        firstName = stringList[0]
        lastName = stringList[1]
        street = stringList[2]
        houseNumber = stringList[3]
        postalCode = stringList[4]
        city = stringList[5]
        phoneNumber = stringList[6]
    }
    return data
}

/**
 * get an string and convert into HostData
 * @param text CSV String convert into HostData
 */
fun textToGuestList(text: String): List<HostData> {
    /*
    make an iterator over stringlist
    make stringlist out of the result string
     */
    val stringList = text.split(";").dropLast(1).iterator()
    val guestList = mutableListOf<HostData>()

    while (stringList.hasNext()) {
        val previousData = guestList.lastOrNull()
        val data = HostData()
        data.apply {
            firstName = stringList.next()
            lastName = stringList.next()
            street = when (val s = stringList.next()) {
                "" -> previousData?.street!!
                else -> s
            }
            houseNumber = when (val s = stringList.next()) {
                "" -> previousData?.houseNumber!!
                else -> s
            }
            postalCode = when (val s = stringList.next()) {
                "" -> previousData?.postalCode!!
                else -> s
            }
            city = when (val s = stringList.next()) {
                "" -> previousData?.city!!
                else -> s
            }
            phoneNumber = stringList.next()
        }
        guestList.add(data)
    }

    return guestList
}