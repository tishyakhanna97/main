package seedu.address.diaryfeature.storage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.diaryfeature.logic.parser.exceptions.DiaryEntryParseException;
import seedu.address.diaryfeature.model.Details;
import seedu.address.diaryfeature.model.DiaryBook;
import seedu.address.diaryfeature.model.diaryEntry.DiaryEntry;
import seedu.address.diaryfeature.model.modelExceptions.UnknownUserException;

@JsonRootName(value = "diaryBook")
public class JsonSerializableDiaryBook {


    private final List<JsonAdaptedDiaryEntry> entries = new ArrayList<>();
    private final JsonAdaptedDetails detail;

    /**
     * Constructs a {@code JsonSerializableDiaryBook} with the given entries.
     */
    @JsonCreator
    public JsonSerializableDiaryBook(@JsonProperty("entries") List<JsonAdaptedDiaryEntry> entries,
    @JsonProperty("details") JsonAdaptedDetails detail) {
        this.entries.addAll(entries);
        this.detail = detail;

    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableDiaryBook(DiaryBook source) {
        entries.addAll(source.getDiaryEntryList().stream().map(JsonAdaptedDiaryEntry::new).collect(Collectors.toList()));
        detail = new JsonAdaptedDetails(source.getDetails());
    }

    /**
     * Converts this address book into the addressBookModel's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public DiaryBook toModelType() throws DiaryEntryParseException {
        DiaryBook diaryBook = new DiaryBook();
        for (JsonAdaptedDiaryEntry jsonAdaptedDiaryEntry : entries) {
            DiaryEntry diaryEntry = jsonAdaptedDiaryEntry.toModelType();
            diaryBook.addDiaryEntry(diaryEntry);
        }
        diaryBook.setinnerDetails(detail.toModelType());
        return diaryBook;
    }

}
