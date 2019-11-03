package seedu.address.diaryfeature.logic.commands;

import seedu.address.diaryfeature.model.DiaryModel;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command<DiaryModel> {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(DiaryModel diaryModel) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
