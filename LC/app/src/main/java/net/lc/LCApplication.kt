package net.lc

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.*

/**
 * Created by HP on 11/25/2016.
 */

class LCApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initAwesome()
        Fresco.initialize(this)
    }
    private fun initAwesome() {
        Iconify
                .with(FontAwesomeModule())
                .with(EntypoModule())
                .with(TypiconsModule())
                .with(MaterialModule())
                .with(MaterialCommunityModule())
                .with(MeteoconsModule())
                .with(WeathericonsModule())
                .with(SimpleLineIconsModule())
                .with(IoniconsModule())
    }
}
