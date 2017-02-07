/*  Ritika Maknoor
  CS 1501
  Assignment 5 pt2 */



import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.io.*;
import java.util.*;
import java.math.*;

public class MySign {

    public static void main(String[] args){

        //Generate SHA-256 hash of contents of file; using HashEx.java file provided
        // lazily catch all exceptions...
        try {
            // read in the file to hash
            Path path = Paths.get(args[1]);
            byte[] data = Files.readAllBytes(path);

            // create class instance to create SHA-256 hash
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // process the file
            md.update(data);
            // generate a hash of the file
            byte[] digest = md.digest();

            // convert the bite string to a printable hex representation
            // note that conversion to biginteger will remove any leading 0s in the bytes of the array!
            String result = new BigInteger(1, digest).toString(16);

            // print the hex representation
            //System.out.println(result);

            BigInteger digestValue = new BigInteger(digest);
            BigInteger compareD = (BigInteger.ZERO);
            


            //Sign or verify
            if (args[0].equals("s")){                                           //Sign files
                try{
                //Decrypt using privkey; exit; say error if not found in curr directory
                    String PATH = "privkey.rsa";
                    FileInputStream bigIntsPrivate = new FileInputStream(new File(PATH));
                    ObjectInputStream privateStream = new ObjectInputStream(bigIntsPrivate);
                    BigInteger D = (BigInteger)privateStream.readObject();
                    BigInteger N = (BigInteger)privateStream.readObject();
                    privateStream.close();
                    bigIntsPrivate.close();

                    BigInteger decryptedValue = digestValue.modPow(D, N);
                    compareD = decryptedValue;
                    byte[] decBy = decryptedValue.toByteArray();
                    System.out.println("----Decryption complete.----");

                //Write out signed version of file (contents of original file & decrypted hash)
                    String signedName = args[1] + ".signed";
                    FileOutputStream signedVersion = new FileOutputStream(new File(signedName));
                    signedVersion.write(decBy);                                 //prints decrypted value
                    signedVersion.write(data);                                  //prints contents of original file
                    signedVersion.close();
                }
                catch (FileNotFoundException exception){
                    System.out.println("Error. The file was not found in current directory.");
                    System.exit(0);
                }
            }



            else if (args[0].equals("v")){                                      //Verify signatures
                try{
                //Read content of original file
                    //done before if/elseif
                //Gen SHA-256 hash
                    //done before if/elseif
                //Read decrypted hash
                    String PATH = "pubkey.rsa";
                    FileInputStream bigIntsPublic = new FileInputStream(new File(PATH));
                    ObjectInputStream publicStream = new ObjectInputStream(bigIntsPublic);
                    BigInteger E = (BigInteger)publicStream.readObject();
                    BigInteger N = (BigInteger)publicStream.readObject();
                    publicStream.close();
                    bigIntsPublic.close();

                //Encrypt with pubkey
                    BigInteger encryptedValue = digestValue.modPow(E, N);
                    System.out.println("----Encryption complete.----");

                //Compare 2 hash values; print if signature valid or not
                    if (encryptedValue.equals(compareD) == true){
                        System.out.println("Signature is valid!!");
                    } 
                    else{
                        System.out.println("Signature is not valid.");
                    }
                }
                catch (FileNotFoundException exception){
                    System.out.println("Error. The file was not found in current directory.");
                    System.exit(0);
                }
            }



            else{                                                               //Invalid flag (not v or s)
                System.out.println("Invalid flag.");
            }

        }


        catch(ArrayIndexOutOfBoundsException outofbounds){                      //If don't enter filename as command line argument
            System.out.println("Please input a filename as well.");
        }

        catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
 


