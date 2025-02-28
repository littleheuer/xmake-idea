package io.xmake.project

import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.DirectoryProjectGeneratorBase
import com.intellij.platform.ProjectGeneratorPeer
import io.xmake.icons.XMakeIcons
import io.xmake.utils.SystemUtils
import java.io.File
import javax.swing.Icon


class XMakeDirectoryProjectGenerator : DirectoryProjectGeneratorBase<XMakeConfigData>(),
    CustomStepProjectGenerator<XMakeConfigData> {

    private var peer: XMakeProjectGeneratorPeer? = null

    override fun getName(): String = "XMake"
    override fun getLogo(): Icon = XMakeIcons.XMAKE
    override fun createPeer(): ProjectGeneratorPeer<XMakeConfigData> = XMakeProjectGeneratorPeer().also { peer = it }

    override fun generateProject(project: Project, baseDir: VirtualFile, data: XMakeConfigData, module: Module) {
        // get content entry path
        val contentEntryPath = baseDir.canonicalPath ?: return

        // create empty project
        SystemUtils.Runv(
            listOf(
                SystemUtils.xmakeProgram,
                "create",
                "-P",
                contentEntryPath,
                "-l",
                data.languagesModel,
                "-t",
                data.kindsModel
            )
        )
    }

    override fun createStep(
        projectGenerator: DirectoryProjectGenerator<XMakeConfigData>,
        callback: AbstractNewProjectStep.AbstractCallback<XMakeConfigData>
    ): AbstractActionWithPanel = XMakeProjectSettingsStep(projectGenerator)
}
