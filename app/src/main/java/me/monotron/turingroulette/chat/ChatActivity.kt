package me.monotron.turingroulette.chat

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_chat.*
import me.monotron.turingroulette.R
import me.monotron.turingroulette.base.BaseActivity
import javax.inject.Inject

class ChatActivity : BaseActivity() {

    @Inject lateinit var viewModel: ChatViewModel
    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        viewModel.state.observe(this, Observer {
            onViewStateChanged(it)
        })

        setContentView(R.layout.activity_chat)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun onViewStateChanged(state: ChatViewState) {
        when (state) {
            is ChatViewState.EstablishingChatSession -> {
                snackbar = Snackbar.make(root_chat_layout, R.string.chat_connecting_to_server, Snackbar.LENGTH_INDEFINITE)
                snackbar?.show()
            }

            is ChatViewState.EstablishingChatSessionFailed -> {
                snackbar?.dismiss()

                AlertDialog.Builder(this)
                    .setTitle(R.string.network_error_dialog_title)
                    .setMessage(R.string.chat_error_dialog_text)
                    .setPositiveButton(R.string.dialog_ok_button) { dialog, _ ->
                        finish()
                        dialog.dismiss()
                    }
                    .create().show()
            }

            is ChatViewState.EstablishingChatSessionSuccess -> {
                snackbar?.dismiss()
            }

            is ChatViewState.WaitingForStranger -> {
                snackbar = Snackbar.make(root_chat_layout, R.string.chat_waiting_for_stranger, Snackbar.LENGTH_INDEFINITE)
                snackbar?.show()
            }

            is ChatViewState.StrangerHasConnected -> {
                snackbar?.dismiss()
            }
        }
    }
}