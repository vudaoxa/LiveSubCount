package net.lc.models

/**
 * Created by mrvu on 1/21/17.
 */
class ChildSetting(var label: String, var name: Int,
                   var checked: Boolean)

class GroupSettings(var label: Int, var childSettings: MutableList<ChildSetting>) {}