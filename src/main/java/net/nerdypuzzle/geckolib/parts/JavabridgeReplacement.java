package net.nerdypuzzle.geckolib.parts;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import net.mcreator.blockly.data.BlocklyLoader;
import net.mcreator.blockly.data.ExternalTrigger;
import net.mcreator.blockly.java.BlocklyVariables;
import net.mcreator.element.ModElementType;
import net.mcreator.element.types.LivingEntity;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.DataListLoader;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.minecraft.MCItem;
import net.mcreator.minecraft.MinecraftImageGenerator;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.blockly.JavaScriptEventListener;
import net.mcreator.ui.dialogs.AIConditionEditor;
import net.mcreator.ui.dialogs.DataListSelectorDialog;
import net.mcreator.ui.dialogs.MCItemSelectorDialog;
import net.mcreator.ui.dialogs.StringSelectorDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.states.PropertyData;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableType;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavabridgeReplacement {
    private static final Logger LOG = LogManager.getLogger("Blockly JS Bridge");
    private JavaScriptEventListener listener;
    private final Supplier<Boolean> blocklyEvent;
    private final MCreator mcreator;
    private final Object NESTED_LOOP_KEY = new Object();
    public Map<String, String> ext_triggers = new LinkedHashMap<String, String>() {
        {
            this.put("no_ext_trigger", L10N.t("trigger.no_ext_trigger", new Object[0]));
        }
    };

    public JavabridgeReplacement(@Nonnull MCreator mcreator, @Nonnull Supplier<Boolean> blocklyEvent) {
        this.blocklyEvent = blocklyEvent;
        this.mcreator = mcreator;
        List<ExternalTrigger> ar10000 = BlocklyLoader.INSTANCE.getExternalTriggerLoader().getExternalTrigers();
        ar10000.forEach(this::addExternalTrigger);
    }

    public void triggerEvent() {
        boolean success = (Boolean)this.blocklyEvent.get();
        if (success && this.listener != null) {
            this.listener.event();
        }

    }

    public static List<String> loadEntityDataListFromCustomEntity(Workspace workspace, String entityName, Class<? extends PropertyData<?>> type) {
        if (entityName != null) {
            Object ent = workspace.getModElementByName(entityName.replace("CUSTOM:", "")).getGeneratableElement();
            if (ent instanceof LivingEntity entity) {
                if (entity != null) {
                    return entity.entityDataEntries.stream().filter((e) -> {
                        return e.property().getClass().equals(type);
                    }).map((e) -> {
                        return e.property().getName();
                    }).toList();
                }
            }
            else if (ent instanceof AnimatedEntity entity) {
                if (entity != null) {
                    return entity.entityDataEntries.stream().filter((e) -> {
                        return e.property().getClass().equals(type);
                    }).map((e) -> {
                        return e.property().getName();
                    }).toList();
                }
            }
        }

        return new ArrayList();
    }

    public String getMCItemURI(String name) {
        ImageIcon base = new ImageIcon(ImageUtils.resize(MinecraftImageGenerator.generateItemSlot(), 36, 36));
        ImageIcon image;
        if (name != null && !name.isEmpty() && !name.equals("null")) {
            image = ImageUtils.drawOver(base, MCItem.getBlockIconBasedOnName(this.mcreator.getWorkspace(), name), 2, 2, 32, 32);
        } else {
            image = base;
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(ImageUtils.toBufferedImage(image.getImage()), "PNG", os);
            return "data:image/png;base64," + Base64.getMimeEncoder().encodeToString(os.toByteArray());
        } catch (Exception var5) {
            LOG.error(var5.getMessage(), var5);
            return "";
        }
    }

    public void openMCItemSelector(String type, JSObject callback) {
        SwingUtilities.invokeLater(() -> {
            MCItem selected = MCItemSelectorDialog.openSelectorDialog(this.mcreator, "allblocks".equals(type) ? ElementUtil::loadBlocks : ElementUtil::loadBlocksAndItems);
            Platform.runLater(() -> {
                Platform.exitNestedEventLoop(this.NESTED_LOOP_KEY, selected != null ? selected.getName() : null);
            });
        });
        String retval = (String)Platform.enterNestedEventLoop(this.NESTED_LOOP_KEY);
        callback.call("callback", new Object[]{retval});
    }

    public void openAIConditionEditor(String data, JSObject callback) {
        SwingUtilities.invokeLater(() -> {
            List<String> retval = AIConditionEditor.open(this.mcreator, data.split(","));
            Platform.runLater(() -> {
                Platform.exitNestedEventLoop(this.NESTED_LOOP_KEY, StringUtils.join(retval, ','));
            });
        });
        String retval = (String)Platform.enterNestedEventLoop(this.NESTED_LOOP_KEY);
        callback.call("callback", new Object[]{retval});
    }

    private String[] openDataListEntrySelector(Function<Workspace, List<DataListEntry>> entryProvider, String type) {
        SwingUtilities.invokeLater(() -> {
            String[] retval = new String[]{"", L10N.t("blockly.extension.data_list_selector.no_entry", new Object[0])};
            DataListEntry selected = DataListSelectorDialog.openSelectorDialog(this.mcreator, entryProvider, L10N.t("dialog.selector.title", new Object[0]), L10N.t("dialog.selector." + type + ".message", new Object[0]));
            if (selected != null) {
                retval[0] = selected.getName();
                retval[1] = selected.getReadableName();
            }

            Platform.runLater(() -> {
                Platform.exitNestedEventLoop(this.NESTED_LOOP_KEY, retval);
            });
        });
        return (String[])Platform.enterNestedEventLoop(this.NESTED_LOOP_KEY);
    }

    private String[] openStringEntrySelector(Function<Workspace, String[]> entryProvider, String type) {
        SwingUtilities.invokeLater(() -> {
            String[] retval = new String[]{"", L10N.t("blockly.extension.data_list_selector.no_entry", new Object[0])};
            String selected = StringSelectorDialog.openSelectorDialog(this.mcreator, entryProvider, L10N.t("dialog.selector.title", new Object[0]), L10N.t("dialog.selector." + type + ".message", new Object[0]));
            if (selected != null) {
                retval[0] = selected;
                retval[1] = selected;
                Platform.runLater(() -> {
                    Platform.exitNestedEventLoop(this.NESTED_LOOP_KEY, retval);
                });
            }

        });
        return (String[])Platform.enterNestedEventLoop(this.NESTED_LOOP_KEY);
    }

    public void openEntrySelector(@Nonnull String type, @Nullable String typeFilter, @Nullable String customEntryProviders, JSObject callback) {
        String[] var10000;
        switch (type) {
            case "entity":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.loadAllEntities(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "entity");
                break;
            case "spawnableEntity":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.loadAllSpawnableEntities(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "entity");
                break;
            case "customEntity":
                var10000 = this.openDataListEntrySelector(ElementUtil::loadCustomEntities, "entity");
                break;
            case "entitydata_logic":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])loadEntityDataListFromCustomEntity(w, customEntryProviders, PropertyData.LogicType.class).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "entity_data");
                break;
            case "entitydata_integer":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])loadEntityDataListFromCustomEntity(w, customEntryProviders, PropertyData.IntegerType.class).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "entity_data");
                break;
            case "entitydata_string":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])loadEntityDataListFromCustomEntity(w, customEntryProviders, PropertyData.StringType.class).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "entity_data");
                break;
            case "gui":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])ElementUtil.loadBasicGUIs(w).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "gui");
                break;
            case "biome":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.loadAllBiomes(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "biome");
                break;
            case "dimension":
                var10000 = this.openStringEntrySelector(ElementUtil::loadAllDimensions, "dimension");
                break;
            case "dimensionCustom":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])w.getModElements().stream().filter((m) -> {
                        return m.getType() == ModElementType.DIMENSION;
                    }).map((m) -> {
                        return "CUSTOM:" + m.getName();
                    }).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "dimension");
                break;
            case "fluid":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.loadAllFluids(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "fluids");
                break;
            case "gamerulesboolean":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.getAllBooleanGameRules(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "gamerules");
                break;
            case "gamerulesnumber":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.getAllNumberGameRules(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "gamerules");
                break;
            case "sound":
                var10000 = this.openStringEntrySelector(ElementUtil::getAllSounds, "sound");
                break;
            case "structure":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])w.getFolderManager().getStructureList().toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "structures");
                break;
            case "procedure":
                var10000 = this.openStringEntrySelector((w) -> {
                    return (String[])w.getModElements().stream().filter((mel) -> {
                        return mel.getType() == ModElementType.PROCEDURE;
                    }).map(ModElement::getName).toArray((x$0) -> {
                        return new String[x$0];
                    });
                }, "procedure");
                break;
            case "arrowProjectile":
                var10000 = this.openDataListEntrySelector((w) -> {
                    return ElementUtil.loadArrowProjectiles(w).stream().filter((e) -> {
                        return e.isSupportedInWorkspace(w);
                    }).toList();
                }, "projectiles");
                break;
            default:
                if (type.startsWith("procedure_retval_")) {
                    VariableType variableType = VariableTypeLoader.INSTANCE.fromName(StringUtils.removeStart(type, "procedure_retval_"));
                    var10000 = this.openStringEntrySelector((w) -> {
                        return ElementUtil.getProceduresOfType(w, variableType);
                    }, "procedure");
                } else {
                    var10000 = !DataListLoader.loadDataList(type).isEmpty() ? this.openDataListEntrySelector((w) -> {
                        return ElementUtil.loadDataListAndElements(w, type, true, typeFilter, StringUtils.split(customEntryProviders, ','));
                    }, type) : new String[]{"", L10N.t("blockly.extension.data_list_selector.no_entry", new Object[0])};
                }
        }

        String[] retval = var10000;
        callback.call("callback", new Object[]{retval[0], retval[1]});
    }

    public void addExternalTrigger(ExternalTrigger external_trigger) {
        this.ext_triggers.put(external_trigger.getID(), external_trigger.getName());
    }

    public String t(String key) {
        return L10N.t(key, new Object[0]);
    }

    public String getGlobalTriggers() {
        return (new Gson()).toJson(this.ext_triggers, Map.class);
    }

    public String[] getListOf(String type) {
        return getListOfForWorkspace(this.mcreator.getWorkspace(), type);
    }

    public static String[] getListOfForWorkspace(Workspace workspace, String type) {
        Object retval;
        switch (type) {
            case "procedure":
                retval = (List)workspace.getModElements().stream().filter((mel) -> {
                    return mel.getType() == ModElementType.PROCEDURE;
                }).map(ModElement::getName).collect(Collectors.toList());
                break;
            case "entity":
                return (String[])ElementUtil.loadAllEntities(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "spawnableEntity":
                return (String[])ElementUtil.loadAllSpawnableEntities(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "gui":
                retval = ElementUtil.loadBasicGUIs(workspace);
                break;
            case "achievement":
                return (String[])ElementUtil.loadAllAchievements(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "effect":
                return (String[])ElementUtil.loadAllPotionEffects(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "potion":
                return (String[])ElementUtil.loadAllPotions(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "gamerulesboolean":
                return (String[])ElementUtil.getAllBooleanGameRules(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "gamerulesnumber":
                return (String[])ElementUtil.getAllNumberGameRules(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "fluid":
                return (String[])ElementUtil.loadAllFluids(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "sound":
                return ElementUtil.getAllSounds(workspace);
            case "particle":
                return (String[])ElementUtil.loadAllParticles(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "direction":
                return ElementUtil.loadDirections();
            case "schematic":
                retval = workspace.getFolderManager().getStructureList();
                break;
            case "enhancement":
                return (String[])ElementUtil.loadAllEnchantments(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "biome":
                return (String[])ElementUtil.loadAllBiomes(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            case "dimension":
                return ElementUtil.loadAllDimensions(workspace);
            case "dimension_custom":
                retval = (List)workspace.getModElements().stream().filter((mu) -> {
                    return mu.getType() == ModElementType.DIMENSION;
                }).map((mu) -> {
                    return "CUSTOM:" + mu.getName();
                }).collect(Collectors.toList());
                break;
            case "material":
                retval = (List)ElementUtil.loadMaterials().stream().map(DataListEntry::getName).collect(Collectors.toList());
                break;
            case "villagerprofessions":
                return (String[])ElementUtil.loadAllVillagerProfessions(workspace).stream().map(DataListEntry::getName).toArray((x$0) -> {
                    return new String[x$0];
                });
            default:
                retval = new ArrayList();
        }

        if (!DataListLoader.loadDataList(type).isEmpty()) {
            return ElementUtil.getDataListAsStringArray(type);
        } else {
            if (type.contains("procedure_retval_")) {
                retval = (List)workspace.getModElements().stream().filter((mod) -> {
                    if (mod.getType() == ModElementType.PROCEDURE) {
                        VariableType returnTypeCurrent = mod.getMetadata("return_type") != null ? VariableTypeLoader.INSTANCE.fromName((String)mod.getMetadata("return_type")) : null;
                        return returnTypeCurrent == VariableTypeLoader.INSTANCE.fromName(StringUtils.removeStart(type, "procedure_retval_"));
                    } else {
                        return false;
                    }
                }).map(ModElement::getName).collect(Collectors.toList());
            }

            return ((List)retval).isEmpty() ? new String[]{""} : (String[])((List)retval).toArray(new String[0]);
        }
    }

    public boolean isPlayerVariable(String field) {
        return BlocklyVariables.isPlayerVariableForWorkspace(this.mcreator.getWorkspace(), field);
    }

    public String getReadableNameOf(String value, String type) {
        if (value.startsWith("CUSTOM:")) {
            return value.substring(7);
        } else {
            String datalist;
            switch (type) {
                case "entity":
                case "spawnableEntity":
                    datalist = "entities";
                    break;
                case "biome":
                    datalist = "biomes";
                    break;
                case "arrowProjectile":
                case "projectiles":
                    datalist = "projectiles";
                    break;
                default:
                    return "";
            }

            return DataListLoader.loadDataMap(datalist).containsKey(value) ? ((DataListEntry)DataListLoader.loadDataMap(datalist).get(value)).getReadableName() : "";
        }
    }

    public void setJavaScriptEventListener(JavaScriptEventListener listener) {
        this.listener = listener;
    }
}

