package kyalo.innocent.offlinenotes.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Context.success(message : String) =
        Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()

fun Context.info(message : String) =
        Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()

fun Context.error(message : String) =
        Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()