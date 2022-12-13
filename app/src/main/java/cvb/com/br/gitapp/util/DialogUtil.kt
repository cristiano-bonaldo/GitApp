package cvb.com.br.gitapp.util

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cvb.com.br.gitapp.R

object DialogUtil {

    fun showDialog(
        activity: AppCompatActivity,
        idMsg: Int,
        idBtTitleOK: Int,
        listenerOK: DialogInterface.OnClickListener?,
        idBtTitleCancel: Int?,
        listenerCancel: DialogInterface.OnClickListener?
    ) {
        val msg = activity.getString(idMsg)
        showDialog(activity, msg, idBtTitleOK, listenerOK, idBtTitleCancel, listenerCancel)
    }

    fun showDialog(
        activity: AppCompatActivity,
        msg: String,
        idBtTitleOK: Int,
        listenerOK: DialogInterface.OnClickListener?,
        idBtTitleCancel: Int?,
        listenerCancel: DialogInterface.OnClickListener?
    ) {
        val builder = AlertDialog.Builder(activity)

        builder.apply {
            this.setTitle(R.string.app_name)
            this.setIcon(R.mipmap.ic_launcher)
            this.setMessage(msg)

            val btOKLabel = activity.getString(idBtTitleOK)
            this.setPositiveButton(btOKLabel, listenerOK)

            idBtTitleCancel?.let { idBtCancelTitle ->
                val btCancelTitle = activity.getString(idBtCancelTitle)
                this.setNegativeButton(btCancelTitle, listenerCancel)
            }

            this.setCancelable(false)
            this.show()
        }
    }
}