package pl.pawkrol.academic.ftp.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Created by pawkrol on 5/24/16.
 */
public class TreeListItem extends TreeItem<File>{

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    public TreeListItem(File file){
        super(file);
    }

    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (isFirstTimeChildren){
            isFirstTimeChildren = false;

            super.getChildren().setAll(buildChildren(this));
        }

        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            File f = getValue();
            isLeaf = f.isFile();
        }

        return isLeaf;
    }

    private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> treeItem){
        File f = treeItem.getValue();
        if (f != null && f.isDirectory()){
            File[] files = f.listFiles();
            if (files != null){
                ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();

                for (File childFile : files){
                    children.add(new TreeListItem(childFile));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }

}
