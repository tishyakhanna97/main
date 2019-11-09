package seedu.address.itinerary.model;

import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.chart.XYChart;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.DatePair;
import seedu.address.commons.util.TreeUtil;
import seedu.address.itinerary.model.event.Event;
import seedu.address.itinerary.model.exceptions.ItineraryException;

/**
 * Access the filtered event list of the itinerary.
 */
public class Model {
    public static final Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;
    private Itinerary itinerary;
    private final FilteredList<Event> filteredEvents;
    private final SortedList<Event> sortedEvents;

    public Model() {
        this.itinerary = new Itinerary();
        filteredEvents = new FilteredList<>(this.itinerary.getEventList());
        sortedEvents = new SortedList<>(filteredEvents);
    }

    public void addEvent(Event event) {
        this.itinerary.addEvent(event);
    }

    public void deleteEvent(Event event) {
        this.itinerary.deleteEvent(event);
    }

    public void setEvent(Event eventToEdit, Event editedEvent) throws ItineraryException {
        CollectionUtil.requireAllNonNull(eventToEdit, editedEvent);

        itinerary.setEvent(eventToEdit, editedEvent);
    }

    public ArrayList<String> getActionList() {
        return itinerary.getActionList();
    }

    /**
     * Returns an unmodifiable view of the list of {@code Expense}
     * @return
     */
    public SortedList<Event> getSortedEventList() {
        return sortedEvents;
    }

    /**
     * Filter out the events in the event list base on the predicate.
     * @param predicate the condition use to filter out the events.
     */
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    public void setItinerary(ReadOnlyItinerary readOnlyItinerary) {
        this.itinerary.resetData(readOnlyItinerary);
    }

    public ReadOnlyItinerary getItinerary() {
        return itinerary;
    }

    /**
     * Mark the specified event in the itinerary event list as done.
     * @param target the specified event to be marked done.
     * @param doneEvent the event with the attribute mark done.
     */
    public void doneEvent(Event target, Event doneEvent) {
        CollectionUtil.requireAllNonNull(target, doneEvent);

        itinerary.doneEvent(target, doneEvent);
    }

    /**
     * Check whether the event list in the itinerary contain the specified event.
     * @param editedEvent the newly created event with the fields changed.
     * @return a boolean whether the event exists in the event list of the itinerary.
     */
    public boolean hasEvent(Event editedEvent) {
        requireNonNull(editedEvent);
        return itinerary.hasEvent(editedEvent);
    }

    /**
     * Add the current action call into the action list.
     * @param commandText the command input that is given by the user.
     */
    public void addAction(String commandText) {
        requireNonNull(commandText);

        itinerary.addAction(commandText);
        itinerary.resetPointer();
    }

    public void clearEvent() {
        itinerary.clear();
    }

    public int getTotalItineraryEntries() {
        return filteredEvents.size();
    }

    public XYChart.Series<String, Number> getItineraryLineChart() {
        SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        TreeUtil<DatePair> treeUtil = new TreeUtil<>();
        FXCollections.unmodifiableObservableList(filteredEvents).stream().forEach(event -> {
            try {
                Date date = dateParser.parse(event.getDate().toString());
                String displayDate = dateFormatter.format(date);
                DatePair defaultIfMissing = new DatePair(0, date);
                Function<DatePair, DatePair> incrementFunction =
                        datePair -> new DatePair(datePair.getKey() + 1, datePair.getValue());
                treeUtil.add(displayDate, defaultIfMissing, incrementFunction);
            } catch (ParseException e) {
                throw new RuntimeException();
            }
        });

        series.getData().addAll(treeUtil.ascendingStream()
                .map(datePair ->
                        new XYChart.Data<String, Number> (
                                dateFormatter.format(datePair.getValue()), datePair.getKey()))
                .collect(Collectors.toList()));
        return series;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Model // instanceof handles nulls
                && sortedEvents.equals(((Model) other).sortedEvents));
    }
}
