package com.bce;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.aspose.email.FolderInfo;
import com.aspose.email.FolderInfoCollection;
import com.aspose.email.MapiMessage;
import com.aspose.email.MessageInfo;
import com.aspose.email.MessageInfoCollection;
import com.aspose.email.PersonalStorage;
import com.aspose.email.SaveOptions;

public class PstToEmailRecuression {
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
		process(pst, folderInfoCollection, outputFolder);

	}

	private static Set<String> allFile = new HashSet<>();

	private static void process(PersonalStorage pst, FolderInfoCollection folderInfoCollection, String outputFolder) {
		for (int i = 0; i < folderInfoCollection.size(); i++) {
			System.out.println("---------------Folder Details Start--------------------");
			FolderInfo folderInfo = (FolderInfo) folderInfoCollection.get_Item(i);
			System.out.println("Folder: " + folderInfo.getDisplayName());
			System.out.println("Total items: " + folderInfo.getContentCount());
			System.out.println(folderInfo.getSubFolders());
			System.out.println("Total items: " + folderInfo.getSubFolders());
			System.out.println("Total unread items: " + folderInfo.getContentUnreadCount());
			System.out.println("---------------Folder Details End--------------------");
			MessageInfoCollection messageList = folderInfoCollection.get(i).getContents();
			System.out.println("Total Message In folder: " + messageList.size());

			for (int j = 0; j < messageList.size(); j++) {
				System.out.println("--Start Reading Folder " + folderInfo.getDisplayName() + " message of " + (j + 1));
				MessageInfo msgInfo = (MessageInfo) messageList.get(j);
				MapiMessage mapi = pst.extractMessage(msgInfo);
				ifNotExist(outputFolder + "\\" + folderInfo.getDisplayName());
				String fileName=j + ".eml";
				String outFile = outputFolder + "\\" + j + ".eml";
				System.out.println("File write location : " + outFile);
				allFile.add(fileName);
				mapi.save(outFile, SaveOptions.getDefaultEml());
				System.out.println("--End Reading Folder " + folderInfo.getDisplayName() + " message of " + (j + 1));

			}
			if (allFile.size() > 0) {
				Path sourceDirectory = Paths.get(outputFolder);
				Path destinationDirectory = Paths.get("C:\\software\\export\\Inbox\\test");

				MoveFiles.moveFileSourceToDest(allFile, sourceDirectory, destinationDirectory);
			}
			if (folderInfo.hasSubFolders()) {
				FolderInfoCollection folderInfoCollectionSub = folderInfo.getSubFolders();
				process(pst, folderInfoCollectionSub, outputFolder + File.separator + folderInfo.getDisplayName());
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
