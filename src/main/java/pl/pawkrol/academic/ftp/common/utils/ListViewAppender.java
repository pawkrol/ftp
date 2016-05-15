package pl.pawkrol.academic.ftp.common.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by pawkrol on 3/7/16.
 */

@Plugin(
        name = "ListViewAppender",
        category = "Core",
        elementType = "appender",
        printObject = true
)
public final class ListViewAppender extends AbstractAppender{

    private static ListView<LogWrapper> listView;
    private static ObservableList<LogWrapper> logs = FXCollections.observableArrayList();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();

    protected ListViewAppender(String name, Filter filter,
                               Layout<? extends Serializable> layout,
                               final boolean ignoreExceptions){
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        readLock.lock();

        final LogWrapper message = new LogWrapper(
                new String(getLayout().toByteArray(event)),
                event.getLevel()
        );

        try {
            Platform.runLater(() -> {
                if (listView != null){


                    logs.add(message);
                    if (logs.size() > 1) {
                        listView.scrollTo(logs.size() - 1);
                    }
                    listView.setItems(logs);

                    listView.setCellFactory(param -> new CellTextColor());
                }
            });
        } catch (IllegalStateException e){
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    @PluginFactory
    public static ListViewAppender createAppender(
                  @PluginAttribute("name") String name,
                  @PluginElement("Layout") Layout<? extends Serializable> layout,
                  @PluginElement("Filter") final Filter filter
    ){
        if (name == null){
            LOGGER.error("No name provided for ListViewAppender");
            return null;
        }

        if (layout == null){
            layout = PatternLayout.createDefaultLayout();
        }

        return new ListViewAppender(name, filter, layout, true);
    }

    public static void setListView(ListView<LogWrapper> listView) {
        ListViewAppender.listView = listView;
    }

    private static class CellTextColor extends ListCell<LogWrapper>{
        @Override
        protected void updateItem(LogWrapper item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                setText(item.getMessage());

                if (item.getLevel() == Level.WARN){
                    setStyle("-fx-text-fill: orange");
                } else if (item.getLevel() == Level.DEBUG){
                    setStyle("-fx-text-fill: slategrey");
                } else if (item.getLevel() == Level.ERROR
                        || item.getLevel() == Level.FATAL){
                    setStyle("-fx-text-fill: red");
                } else {
                    setStyle("-fx-text-fill: black");
                }
            }
        }
    }
}
