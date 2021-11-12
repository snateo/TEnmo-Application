package com.techelevator.view;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferWithUsernames;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ClientAccountService;

import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		out.println("+--------------------------------+");
		out.println("| TEnmo™ Option Menu             |");
		out.println("+--------------------------------+");
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println("| " + optionNum + ") " + options[i]);
		}
		out.println("+--------------------------------+");
		out.println("Please choose an option >>> ");
		out.flush();
	}

	public void displayUserList(User[] userList) {
		System.out.println("+---------+--------------------------+");
		System.out.println("| Send TE Bucks to TEnmo™ User       |");
		System.out.println("+---------+--------------------------+");
		System.out.println("| User ID | Name                     |");
		System.out.println("+---------+--------------------------+");
		for (User user : userList) {
			String rawUsername = user.getUsername();
			String niceUsername = rawUsername.substring(0, 1).toUpperCase() + rawUsername.substring(1);
			System.out.println("| " + user.getId() + "    | " +
					niceUsername );
		}
		System.out.println("+---------+--------------------------+");
	}

	public void displayTransferList(TransferWithUsernames[] transferList) {
		System.out.println("+------------------------------------+");
		System.out.println("| Transfer List                      |");
		System.out.println("+------------------------------------+");
		for (TransferWithUsernames transfer : transferList) {
			System.out.println("| Transfer ID: " + transfer.getTransfer().getTransferId());
			String niceSenderUsername = transfer.getSenderUsername().substring(0, 1).toUpperCase() + transfer.getSenderUsername().substring(1);
			System.out.println("| From:        " + niceSenderUsername);
			String niceRecipientUsername = transfer.getRecipientUsername().substring(0, 1).toUpperCase() + transfer.getRecipientUsername().substring(1);
			System.out.println("| To:          " + niceRecipientUsername);
			System.out.println("| Amount:      " + transfer.getTransfer().getAmount());
			System.out.println("+------------------------------------+");
		}
	}

	public void displayTransferDetails(TransferWithUsernames transfer) {

		String statusType = null;
		String transferType = null;

		if (transfer.getTransfer().getTransferTypeId() == 1) {
			transferType = "Request";
		} else if (transfer.getTransfer().getTransferTypeId() == 2) {
			transferType = "Send";
		} else {
			transferType = "Transfer Type Error. Database Corrupted. Please contact customer support.";
		}


		if (transfer.getTransfer().getTransferStatusId() == 1) {
			statusType = "Pending";
		} else if (transfer.getTransfer().getTransferStatusId() == 2) {
			statusType = "Approved";
		} else if (transfer.getTransfer().getTransferStatusId() == 3) {
			statusType = "Rejected";
		} else {
			statusType = "Transfer Status Error. Database Corrupted. Please contact customer support.";
		}


		System.out.println("+------------------------------------+");
		System.out.println("| Transfer Details                   |");
		System.out.println("+------------------------------------+");
		System.out.println("| ID:       " + transfer.getTransfer().getTransferId());
		String niceSenderUsername = transfer.getSenderUsername().substring(0, 1).toUpperCase() + transfer.getSenderUsername().substring(1);
		System.out.println("| From:     " + niceSenderUsername);
		String niceRecipientUsername = transfer.getRecipientUsername().substring(0, 1).toUpperCase() + transfer.getRecipientUsername().substring(1);
		System.out.println("| To:       " + niceRecipientUsername);
		System.out.println("| Type:     " + transferType);
		System.out.println("| Status:   " + statusType);
		System.out.println("| Amount:   " + transfer.getTransfer().getAmount());
		System.out.println("+------------------------------------+");

	}


	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Long getUserInputLong(String prompt) {
		Long result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				Toolkit.getDefaultToolkit().beep();
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				Toolkit.getDefaultToolkit().beep();
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt) {
		Double result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Double.parseDouble(userInput);
			} catch(NumberFormatException e) {
				Toolkit.getDefaultToolkit().beep();
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}




	public void printTransfers(int transferId, String fromOrTo, BigDecimal amount) {
		out.println(transferId + "     " + fromOrTo + "          " + "$ " + amount);
	}

	public void printApproveOrRejectOptions() {
		out.println("1: Approve");
		out.println("2: Reject");
		out.println("0: Don't approve or reject\n");
	}


}
