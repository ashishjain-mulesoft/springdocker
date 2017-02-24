package hello;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class Application {
	private static String bucketName     = "wwhs-liferay";
	private static String CONTENT_TYPE = "text/plain";
	
	public static void main(String[] args) throws IOException {
	    AWSCredentials credentials = new BasicAWSCredentials("AKIAI22S5GW5MULDQP3Q", "FcunNus+dJRHHlDmSmzEHRtvHEl21iQavADF6zza");
        AmazonS3 s3client = new AmazonS3Client(credentials);
        try {
        	UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();;
            byte[] fileContentBytes = randomUUIDString.getBytes(StandardCharsets.UTF_8);
            InputStream fileInputStream = new ByteArrayInputStream(fileContentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(CONTENT_TYPE);
            metadata.setContentLength(fileContentBytes.length);
            System.out.println("Uploading a new object to S3 from a file\n");
         
            String folderName = "testfolder";
    		createFolder(bucketName, folderName, s3client);
    		
    		// upload file to folder and set it to public
    		String fileName = folderName + "/" + randomUUIDString+".txt";
    		s3client.putObject(new PutObjectRequest(bucketName, fileName,fileInputStream,metadata)
    				.withCannedAcl(CannedAccessControlList.PublicRead));
    		

         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
	
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + "/", emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

}
