package ca.hobin.bugdemo

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject

private const val KEY_PROXY_SETTINGS = "ProxySettings"
private const val KEY_PROXY_MODE = "ProxyMode"
private const val KEY_PROXY_PAC = "ProxyPacUrl"
private const val KEY_PROXY_BYPASS = "ProxyBypassList"

class MainActivity : AppCompatActivity() {
    private val adminComponent by lazy { ComponentName(this, DeviceAdmin::class.java) }
    private val devicePolicyManager by lazy { getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    private val setProxyWithBypassButton: View by lazy { findViewById(R.id.set_proxy_with_bypass) }
    private val setProxyWithoutBypassButton: View by lazy { findViewById(R.id.set_proxy_without_bypass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setProxyWithBypassButton.setOnClickListener(this::setProxyWithBypass)
        setProxyWithoutBypassButton.setOnClickListener(this::setProxyWithoutBypass)
    }

    private fun setProxyWithBypass(view: View?) {
        val jsonObject = JSONObject()

        try {
            jsonObject.put(KEY_PROXY_MODE, "pac_script")
            jsonObject.put(KEY_PROXY_PAC, "http://pac.zscaler.net/dhl.com/pac.pac")
            jsonObject.put(KEY_PROXY_BYPASS, "example.com")
        } catch (e: JSONException) {
            Log.e("BugDemo", "Well, this shouldn't have happened");
        }

        // JSONObject insists on escaping forward-slashes, we need to unescape them for this to work
        val jsonString = jsonObject.toString().replace("\\/", "/")

        val bundle = Bundle()
        bundle.putString(KEY_PROXY_SETTINGS, jsonString)

        setChromeRestrictions(bundle)
    }

    private fun setProxyWithoutBypass(view: View?) {
        val jsonObject = JSONObject()

        try {
            jsonObject.put(KEY_PROXY_MODE, "pac_script")
            jsonObject.put(KEY_PROXY_PAC, "http://pac.zscaler.net/dhl.com/pac.pac")
        } catch (e: JSONException) {
            Log.e("BugDemo", "Well, this shouldn't have happened");
        }

        // JSONObject insists on escaping forward-slashes, we need to unescape them for this to work
        val jsonString = jsonObject.toString().replace("\\/", "/")

        val bundle = Bundle()
        bundle.putString(KEY_PROXY_SETTINGS, jsonString)

        setChromeRestrictions(bundle)
    }

    private fun setChromeRestrictions(bundle: Bundle) {
        devicePolicyManager.setApplicationRestrictions(adminComponent, "com.android.chrome", bundle)
    }
}
