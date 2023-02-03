package net.nerdypuzzle.geckolib.parts;

import net.mcreator.generator.GeneratorUtils;
import net.mcreator.io.FileIO;
import net.mcreator.io.Transliteration;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.action.impl.workspace.resources.ModelImportActions;
import net.mcreator.ui.dialogs.file.FileDialogs;
import net.mcreator.ui.init.L10N;
import net.nerdypuzzle.geckolib.registry.PluginActions;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PluginModelActions extends ModelImportActions {

    public static File getAnimationsDir(MCreator mcreator) {
        return new File(GeneratorUtils.getSpecificRoot(mcreator.getWorkspace(), mcreator.getWorkspace().getGeneratorConfiguration(), "mod_assets_root"), "animations/");
    }

    public static File getGeometryDir(MCreator mcreator) {
        return new File(GeneratorUtils.getSpecificRoot(mcreator.getWorkspace(), mcreator.getWorkspace().getGeneratorConfiguration(), "mod_assets_root"), "geo/");
    }

    public static File getDisplaySettingsDir(MCreator mcreator) {
        return new File(GeneratorUtils.getSpecificRoot(mcreator.getWorkspace(), mcreator.getWorkspace().getGeneratorConfiguration(), "mod_assets_root"), "models/displaysettings/");
    }

    private static List<File> listModelsInDir(@Nullable File dir) {
        if (dir == null) {
            return Collections.emptyList();
        } else {
            List<File> retval = new ArrayList();
            File[] block = dir.listFiles();
            File[] var3 = block != null ? block : new File[0];
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File element = var3[var5];
                if (element.getName().endsWith(".geo.json")) {
                    retval.add(element);
                }
            }

            return retval;
        }
    }

    private static List<File> listDisplaySettingsInDir(@Nullable File dir) {
        if (dir == null) {
            return Collections.emptyList();
        } else {
            List<File> retval = new ArrayList();
            File[] block = dir.listFiles();
            File[] var3 = block != null ? block : new File[0];
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File element = var3[var5];
                if (element.getName().endsWith(".json")) {
                    retval.add(element);
                }
            }

            return retval;
        }
    }
    public static List<File> getGeomodels(MCreator mcreator) {
        return listModelsInDir(getGeometryDir(mcreator));
    }

    public static List<File> getDisplaysettings(MCreator mcreator) {
        return listDisplaySettingsInDir(getDisplaySettingsDir(mcreator));
    }

    public static class GECKOLIB extends BasicAction {
        public GECKOLIB(PluginActions actionRegistry) {
            super(actionRegistry, L10N.t("action.workspace.resources.import_geckolib_model"), actionEvent -> {
                File geoModel = FileDialogs.getOpenDialog(actionRegistry.getMCreator(), new String[] { ".geo.json" });
                if (geoModel != null)
                    importGeckoLibModels(actionRegistry.getMCreator(), geoModel);
            });
        }
    }

    public static class DISPLAYSETTINGS extends BasicAction {
        public DISPLAYSETTINGS(PluginActions actionRegistry) {
            super(actionRegistry, L10N.t("action.workspace.resources.import_display_settings"), actionEvent -> {
               File displaySettings = FileDialogs.getOpenDialog(actionRegistry.getMCreator(), new String[] { ".json" });
               if (displaySettings != null)
                   importDisplaySettings(actionRegistry.getMCreator(), displaySettings);
            });
        }
    }

    public static void importGeckoLibModels(MCreator mcreator, File geoModel) {
        FileIO.copyFile(geoModel, new File(PluginModelActions.getGeometryDir(mcreator),
                Transliteration.transliterateString(geoModel.getName()).toLowerCase(Locale.ENGLISH).trim()
                        .replace(":", "").replace(" ", "_")));
        File[] animations = FileDialogs.getMultiOpenDialog(mcreator, new String[] { ".animation.json" });
        for (File animation : animations) {
            FileIO.copyFile(animation, new File(PluginModelActions.getAnimationsDir(mcreator),
                    Transliteration.transliterateString(animation.getName()).toLowerCase(Locale.ENGLISH).trim()
                            .replace(":", "").replace(" ", "_")));
        }
    }

    public static void importDisplaySettings(MCreator mcreator, File displaySettings) {
        FileIO.copyFile(displaySettings, new File(PluginModelActions.getDisplaySettingsDir(mcreator),
                Transliteration.transliterateString(displaySettings.getName()).toLowerCase(Locale.ENGLISH).trim()
                        .replace(":", "").replace(" ", "_")));
    }

}
