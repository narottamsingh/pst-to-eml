package com.bce;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.aspose.email.FolderInfo;
import com.aspose.email.FolderInfoCollection;
import com.aspose.email.MapiMessage;
import com.aspose.email.MessageInfo;
import com.aspose.email.MessageInfoCollection;
import com.aspose.email.PersonalStorage;
import com.aspose.email.SaveOptions;

public class PstToEml {
	public static void main(String[] args) {
		System.out.println("Length is " + args.length);
		if (args.length < 1) {
			System.out.println("Please pass the two parameter 1=PST Input file path \n Second is=EML Output folder");
			System.exit(0);
		}
		String psdFilePath = args[0];
		String outputFolder = args[1];
		PersonalStorage pst = PersonalStorage.fromFile(psdFilePath);
		FolderInfoCollection folderInfoCollection = pst.getRootFolder().getSubFolders();
		for (int i = 0; i < folderInfoCollection.size(); i++) {
			System.out.println("---------------Folder Details Start--------------------");
			FolderInfo folderInfo = (FolderInfo) folderInfoCollection.get_Item(i);
			System.out.println("Folder: " + folderInfo.getDisplayName());
			System.out.println("Total items: " + folderInfo.getContentCount());
			System.out.println("Total unread items: " + folderInfo.getContentUnreadCount());
			System.out.println("---------------Folder Details End--------------------");
			MessageInfoCollection messageList = folderInfoCollection.get(i).getContents();
			System.out.println("Total Message In folder: " + messageList.size());
			for (int j = 0; j < messageList.size(); j++) {
				System.out.println("--Start Reading Folder " + folderInfo.getDisplayName() + " message of " + (j + 1));
				MessageInfo msgInfo = (MessageInfo) messageList.get(j);
				MapiMessage mapi = pst.extractMessage(msgInfo);
				ifNotExist(outputFolder + "\\" + folderInfo.getDisplayName());
				String outFile = outputFolder + "\\" + folderInfo.getDisplayName() + "\\" + j + ".eml";
				System.out.println("File write location : " + outFile);
				mapi.save(outFile, SaveOptions.getDefaultEml());
				System.out.println("--End Reading Folder " + folderInfo.getDisplayName() + " message of " + (j + 1));

			}
		}
	}

	public static void ifNotExist(String folderPath) {
		// Convert the folderPath string to a Path object
		Path folder = Paths.get(folderPath);
		// Check if the folder doesn't exist
		if (!Files.exists(folder)) {
			try {
				// Create the folder
				Files.createDirectories(folder);
				System.out.println("Folder created successfully.");
			} catch (Exception e) {
				System.err.println("Error creating folder: " + e.getMessage());
			}
		} else {
			System.out.println("Folder already exists.");
		}
	}

}
