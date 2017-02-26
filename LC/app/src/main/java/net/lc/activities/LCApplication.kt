package net.lc.activities

import android.support.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import com.joanzapata.iconify.fonts.IoniconsModule
import net.lc.presenters.SettingsPresenter
import net.lc.presenters.initRealm
import net.lc.utils.initAds
import net.lc.utils.initAnimations
import net.lc.utils.initIcons
import net.lc.utils.initTracking
import vn.mycare.member.utils.TimeUtils

/**
 * Created by HP on 11/25/2016.
 */

class LCApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initAwesome()
        Fresco.initialize(this)
        initRealm(this)
        SettingsPresenter.initSettings(this)
        initAnimations(this)
        initIcons(this)
        TimeUtils
        initTracking(this)
        initAds(this)
    }

    private fun initAwesome() {
        Iconify.with(FontAwesomeModule())
                .with(IoniconsModule())
//                .with(EntypoModule())
//                .with(TypiconsModule())
//                .with(MaterialModule())
//                .with(MaterialCommunityModule())
//                .with(MeteoconsModule())
//                .with(WeathericonsModule())
//                .with(SimpleLineIconsModule())

    }
}
