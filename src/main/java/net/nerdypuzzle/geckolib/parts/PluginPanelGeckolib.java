package net.nerdypuzzle.geckolib.parts;

import net.mcreator.ui.component.TransparentToolBar;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.laf.SlickDarkScrollBarUI;
import net.mcreator.ui.workspace.IReloadableFilterable;
import net.mcreator.ui.workspace.WorkspacePanel;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.resources.Model;
import net.nerdypuzzle.geckolib.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class PluginPanelGeckolib extends JPanel implements IReloadableFilterable {
    private final WorkspacePanel workspacePanel;
    private final FilterModel listmodel = new FilterModel();
    private final JList<File> modelList;
    public PluginPanelGeckolib(final WorkspacePanel workspacePanel) {
        super(new BorderLayout());
        this.modelList = new JList(this.listmodel);
        this.setOpaque(false);
        this.workspacePanel = workspacePanel;
        this.modelList.setOpaque(false);
        this.modelList.setSelectionMode(0);
        this.modelList.setLayoutOrientation(2);
        this.modelList.setVisibleRowCount(-1);
        this.modelList.setCellRenderer(new Render());
        this.modelList.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int idx = PluginPanelGeckolib.this.modelList.locationToIndex(e.getPoint());
                File model = (File) PluginModelActions.getGeometryDir(workspacePanel.getMCreator());
                File display = (File) PluginModelActions.getDisplaySettingsDir(workspacePanel.getMCreator());
                if (model != null)
                    workspacePanel.getMCreator().getStatusBar().setMessage(model.getName());
                if (display != null)
                    workspacePanel.getMCreator().getStatusBar().setMessage(display.getName());

            }
        });
        JScrollPane sp = new JScrollPane(this.modelList);
        sp.setOpaque(false);
        sp.setHorizontalScrollBarPolicy(31);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(11);
        sp.getVerticalScrollBar().setUI(new SlickDarkScrollBarUI((Color)UIManager.get("MCreatorLAF.DARK_ACCENT"), (Color)UIManager.get("MCreatorLAF.LIGHT_ACCENT"), sp.getVerticalScrollBar()));
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        this.add("Center", sp);
        TransparentToolBar bar = new TransparentToolBar();
        bar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 0));
        JButton imp1 = L10N.button("action.workspace.resources.import_geckolib_model");
        imp1.setIcon(UIRES.get("16px.importgeckolibmodel"));
        imp1.setContentAreaFilled(false);
        imp1.setOpaque(false);
        imp1.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        imp1.addActionListener(e -> {
            Launcher.ACTION_REGISTRY.importGeckoLibModel.doAction();
            this.reloadElements();
        });
        ComponentUtils.deriveFont(imp1, 12.0F);
        imp1.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        bar.add(imp1);
        JButton imp2 = L10N.button("action.workspace.resources.import_display_settings");
        imp2.setIcon(UIRES.get("16px.importgeckolibmodel"));
        imp2.setContentAreaFilled(false);
        imp2.setOpaque(false);
        imp2.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        imp2.addActionListener(e -> {
            Launcher.ACTION_REGISTRY.importDisplaySettings.doAction();
            this.reloadElements();
        });
        ComponentUtils.deriveFont(imp2, 12.0F);
        imp1.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        bar.add(imp2);
        JButton del = L10N.button("workspace.3dmodels.delete_selected", new Object[0]);
        del.setIcon(UIRES.get("16px.delete.gif"));
        del.setOpaque(false);
        del.setContentAreaFilled(false);
        del.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        bar.add(del);
        del.addActionListener((e) -> {
            this.deleteCurrentlySelected();
        });
        this.modelList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 127) {
                    PluginPanelGeckolib.this.deleteCurrentlySelected();
                }

            }
        });
        this.add("North", bar);
    }

    private void deleteCurrentlySelected() {
        Object file = this.modelList.getSelectedValue();
        String file2 = ((File)file).getAbsolutePath().replace("\\geo\\", "\\animations\\").replace(".geo.", ".animation.");
        if (file != null && file instanceof File model) {
            int n = JOptionPane.showConfirmDialog(this.workspacePanel.getMCreator(), L10N.t("workspace.3dmodels.delete_confirm_message", new Object[0]), L10N.t("common.confirmation", new Object[0]), 0, 3, (Icon)null);
            if (n == 0) {
                ((File) file).delete();
                File file3 = new File(file2);
                if (file3.exists())
                    file3.delete();
                this.reloadElements();
            }
        }

    }

    public void reloadElements() {
        this.listmodel.removeAllElements();
        java.util.List var10000 = PluginModelActions.getGeomodels(workspacePanel.getMCreator());
        List displays = PluginModelActions.getDisplaysettings(workspacePanel.getMCreator());
        PluginPanelGeckolib.FilterModel var10001 = this.listmodel;
        Objects.requireNonNull(var10001);
        for(int i = 0; i <= var10000.size() - 1; i++)
            var10001.addElement((File) var10000.get(i));
        for (int i = 0; i <= displays.size() - 1; i++)
            var10001.addElement((File) displays.get(i));

        this.refilterElements();
    }

    public void refilterElements() {
        this.listmodel.refilter();
    }

    private class FilterModel extends DefaultListModel<File> {
        java.util.List<File> items = new ArrayList();
        List<File> filterItems = new ArrayList();

        FilterModel() {
        }

        public int indexOf(Object elem) {
            return elem instanceof File ? this.filterItems.indexOf(elem) : -1;
        }

        public File getElementAt(int index) {
            return !this.filterItems.isEmpty() && index < this.filterItems.size() ? (File)this.filterItems.get(index) : null;
        }

        public int getSize() {
            return this.filterItems.size();
        }

        public void addElement(File o) {
            this.items.add(o);
            this.refilter();
        }

        public void removeAllElements() {
            super.removeAllElements();
            this.items.clear();
            this.filterItems.clear();
        }

        public boolean removeElement(Object a) {
            if (a instanceof Model) {
                this.items.remove(a);
                this.filterItems.remove(a);
            }

            return super.removeElement(a);
        }

        void refilter() {
            this.filterItems.clear();
            String term = PluginPanelGeckolib.this.workspacePanel.search.getText();
            this.filterItems.addAll(this.items.stream().filter(Objects::nonNull).filter((item) -> {
                return item.getName().toLowerCase(Locale.ENGLISH).contains(term.toLowerCase(Locale.ENGLISH)) || item.getName().toLowerCase(Locale.ENGLISH).contains(term.toLowerCase(Locale.ENGLISH));
            }).toList());
            if (PluginPanelGeckolib.this.workspacePanel.sortName.isSelected()) {
                this.filterItems.sort(Comparator.comparing(File::getName));
            }

            if (PluginPanelGeckolib.this.workspacePanel.desc.isSelected()) {
                Collections.reverse(this.filterItems);
            }

            this.fireContentsChanged(this, 0, this.getSize());
        }
    }

    static class Render extends JLabel implements ListCellRenderer<File> {
        Render() {
        }

        public JLabel getListCellRendererComponent(JList<? extends File> list, File ma, int index, boolean isSelected, boolean cellHasFocus) {
            this.setOpaque(isSelected);
            this.setBackground(isSelected ? (Color)UIManager.get("MCreatorLAF.LIGHT_ACCENT") : (Color)UIManager.get("MCreatorLAF.DARK_ACCENT"));
            this.setText(StringUtils.abbreviateString(ma.getName(), 13));
            this.setToolTipText(ma.getName());
            ComponentUtils.deriveFont(this, 11.0F);
            this.setVerticalTextPosition(3);
            this.setHorizontalTextPosition(0);
            this.setHorizontalAlignment(0);
            this.setIcon(UIRES.get("model.geckolib"));
            this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return this;
        }
    }

}

