package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.InvalidTransferIdChoice;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private UserService userService;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private ClientTransferService transferService;
    private AccountService accountService;
    private ClientAccountService clientAccountService;


    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new UserService(API_BASE_URL), new ClientTransferService(API_BASE_URL), new ClientAccountService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, UserService userService, ClientTransferService transferService, ClientAccountService clientAccountService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.userService = userService;
		this.transferService = transferService;
		this.clientAccountService = clientAccountService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				//viewPendingRequests();
				System.out.println("Not Yet Implemented.");
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		System.out.println("+--------------------------------+");
		System.out.println("| TEnmo™ Account Balance         |");
		System.out.println("+--------------------------------+");
		System.out.println("| Your Current Balance is " + userService.getCurrentBalance(currentUser.getUser().getId()));
		System.out.println("+--------------------------------+");

		console.getUserInput("Press any key to return to TEnmo™ Option Menu");
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Transfer[] transferList = transferService.getTransfersFromUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
		TransferWithUsernames[] transferListWithUsernames = new TransferWithUsernames[transferList.length];
		for (int i = 0; i < transferList.length; i++) {
			transferListWithUsernames[i] = new TransferWithUsernames();
			transferListWithUsernames[i].setTransfer(transferList[i]);
			transferListWithUsernames[i].setSenderUsername(clientAccountService.getUsernameByAccountId(transferList[i].getAccountFrom()));
			transferListWithUsernames[i].setRecipientUsername(clientAccountService.getUsernameByAccountId(transferList[i].getAccountTo()));
		}
		console.displayTransferList(transferListWithUsernames);

		boolean transferToDisplayExists = false;

		while (!transferToDisplayExists) {
			int transferIdToDisplayDetails = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
			if (transferIdToDisplayDetails == 0)
				return;
			System.out.println("You have selected transfer ID " + transferIdToDisplayDetails);

			//check to see if the transferId exists in the transferList

			Transfer transferToDisplay = null;

			for (Transfer transfer : transferList) {
				if (transfer.getTransferId() == transferIdToDisplayDetails) {
					transferToDisplayExists = true;
					transferToDisplay = transfer;
					//System.out.println("It's a match!");
					break;
				}
			}
			if (transferToDisplayExists){
				TransferWithUsernames transferWithUsernamesToDisplay = new TransferWithUsernames();
				transferWithUsernamesToDisplay.setTransfer(transferToDisplay);
				transferWithUsernamesToDisplay.setSenderUsername(clientAccountService.getUsernameByAccountId(transferToDisplay.getAccountFrom()));
				transferWithUsernamesToDisplay.setRecipientUsername(clientAccountService.getUsernameByAccountId(transferToDisplay.getAccountTo()));
				console.displayTransferDetails(transferWithUsernamesToDisplay);
				console.getUserInput("Press any key to return to TEnmo™ Option Menu");
			}


			if (!transferToDisplayExists){
				Toolkit.getDefaultToolkit().beep();
				System.out.println("You have entered an invalid Transfer ID.");
			}

		}


	}

//	private void viewPendingRequests() {
//			Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
//			System.out.println("Pending Transfers:");
//			System.out.println("ID          To          Amount");
//
//
//			for(Transfer transfer: transfers) {
//				printTransferStubDetails(currentUser, transfer);
//			}
//			// TODO ask to view details
//			int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to approve/reject (0 to cancel)");
//			Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
//			if(transferChoice != null) {
//				approveOrReject(transferChoice, currentUser);
//			}
//		}


	private void sendBucks() {
		//Print User List
		console.displayUserList(userService.getUserList());

		//Prompt for recipient ID
		Long recipientID = console.getUserInputLong("Enter Recipient User ID, or 0 to return to TEnmo™ Option Menu");
		if (recipientID == 0)
			return;

		//Verify that User exists
		Long[] userIdList = userService.getUserIdList();
		boolean isValidId = false;

		for (long userId : userIdList) {
			//System.out.println(userId);
			if (userId == recipientID) {
				isValidId = true;
				break;
			}
		}

		System.out.println("");
		System.out.println("+--------------------------+");
		System.out.println("| TEnmo™ User Verification |");
		System.out.println("+--------------------------+");
		String recipientUsername = "";
		String niceRecipientUsername = "";
		if (isValidId){
			recipientUsername = userService.findUsernameByUserId(userService.getUserList(), recipientID);
			niceRecipientUsername = recipientUsername.substring(0, 1).toUpperCase() + recipientUsername.substring(1);
			System.out.println("| You would like initiate a TEnmo™ transfer to " + niceRecipientUsername + " (User #" + recipientID + ")");
		} else {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("| Recipient User ID does not exist.");
			System.out.println("+--------------------------+");
			console.getUserInput("Press any key to return to TEnmo™ Option Menu");
			return;
		}
		System.out.println("| Your current account balance is $"
				+ userService.getCurrentBalance(currentUser.getUser().getId()) + " TE Bucks.");
		System.out.println("| How much would you like to transfer? ");
		System.out.println("+--------------------------------------------------------------------------+");



		//Prompt for Transfer Amount
		Double transferAmount = console.getUserInputDouble("Enter Transfer Amount");
		BigDecimal transferAmountBigDecimal = BigDecimal.valueOf(transferAmount);
		BigDecimal currentAccountBalance = userService.getCurrentBalance(currentUser.getUser().getId());


		System.out.println("");
		System.out.println("+--------------------------------------------------------------------------+");
		System.out.println("| TEnmo™ Confirmation Window                                               |");
		System.out.println("+--------------------------------------------------------------------------+");
		System.out.println("| You would like to send " + niceRecipientUsername + " (User #" + recipientID + ") $"
				+ transferAmount + " TE Bucks. ");
		System.out.println("| Your current account balance is $"
				+ userService.getCurrentBalance(currentUser.getUser().getId()) + " TE Bucks.");

		if (currentAccountBalance.compareTo(transferAmountBigDecimal) >= 0) {
			System.out.println("| After this transaction, your account balance will be " +
					(currentAccountBalance.subtract(transferAmountBigDecimal)) + " TE Bucks.");
			System.out.println("+--------------------------------------------------------------------------+");
		} else {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("| You don't have enough money in your account to complete the transaction.");
			System.out.println("+--------------------------------------------------------------------------+");
			console.getUserInput("Press any key to return to TEnmo™ Option Menu");
			return;
		}


		String confirmationPrompt = console.getUserInput("Type 'Y' to confirm or 'N' to cancel");

		//If confirmation prompt is 'Y' initiate transfer
		System.out.println("");
		if (confirmationPrompt.startsWith("y") || confirmationPrompt.startsWith("Y")) {
			System.out.println("+--------------------------------------------------------------------------+");
			System.out.println("| TEnmo™ Transfer Initiated... Check your email for confirmation!          |");
			System.out.println("+--------------------------------------------------------------------------+");
			console.getUserInput("Press any key to return to TEnmo™ Option Menu");
			Transfer transfer = new Transfer(userService.getAccountIdByUserId(currentUser.getUser().getId()), userService.getAccountIdByUserId(recipientID), transferAmountBigDecimal);
			transferService.createTransfer(currentUser, transfer);
			Account updatedSenderAccount = new Account(userService.getAccountIdByUserId(currentUser.getUser().getId()), currentUser.getUser().getId(), currentAccountBalance.subtract(transferAmountBigDecimal));
			clientAccountService.updateAccountBalance(currentUser, updatedSenderAccount);
			Account updatedRecipientAccount = new Account(userService.getAccountIdByUserId(recipientID), recipientID, userService.getCurrentBalance(recipientID).add(transferAmountBigDecimal));
			clientAccountService.updateAccountBalance(currentUser, updatedRecipientAccount);


		} else {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("+--------------------------------------------------------------------------+");
			System.out.println("| TEnmo™ Transfer Aborted... Better Luck Next Time!                        |");
			System.out.println("+--------------------------------------------------------------------------+");
			console.getUserInput("Press any key to return to TEnmo™ Option Menu");
		}

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				Toolkit.getDefaultToolkit().beep();
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

//	private void printTransferStubDetails(AuthenticatedUser authenticatedUser, Transfer transfer) {
//		String fromOrTo = "";
//		Long accountFrom = transfer.getAccountFrom();
//		Long accountTo = transfer.getAccountTo();
//		if (accountService.getAccountById(currentUser, accountTo).getUserId() == authenticatedUser.getUser().getId()) {
//			int accountFromUserId = accountService.getAccountById(currentUser, accountFrom).getUserId();
//			//String userFromName = ServerUserService.getUserByUserId(currentUser, accountFromUserId).getUsername();
//			fromOrTo = "From: ";// + userFromName;
//		} else {
//			int accountToUserId = accountService.getAccountById(currentUser, accountTo).getUserId();
//			//String userToName = ServerUserService.getUserByUserId(currentUser, accountToUserId).getUsername();
//			fromOrTo = "To: ";// + userToName;
//		}
//
//		console.printTransfers(transfer.getTransferId(), fromOrTo, transfer.getAmount());
//	}

	private Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
		Transfer transferChoice = null;
		if(transferIdChoice != 0) {
			try {
				boolean validTransferIdChoice = false;
				for (Transfer transfer : transfers) {
					if (transfer.getTransferId() == transferIdChoice) {
						validTransferIdChoice = true;
						transferChoice = transfer;
						break;
					}
				}
				if (!validTransferIdChoice) {
					throw new InvalidTransferIdChoice();
				}
			} catch (InvalidTransferIdChoice e) {
				System.out.println(e.getMessage());
			}
		}
		return transferChoice;
	}

	private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser) {
		console.printApproveOrRejectOptions();
		int choice = console.getUserInputInteger("Please choose an option");

		if(choice != 0) {
			if(choice == 1) {
				int transferStatusId = transferService.getTransferStatus(currentUser, "Approved").getTransferStatusId();
				pendingTransfer.setTransferStatusId(transferStatusId);
			} else if (choice == 2) {
				int transferStatusId = transferService.getTransferStatus(currentUser, "Rejected").getTransferStatusId();
				pendingTransfer.setTransferStatusId(transferStatusId);
			} else {
				System.out.println("Invalid choice.");
			}
			transferService.updateTransfer(currentUser, pendingTransfer);
		}

	}
}
