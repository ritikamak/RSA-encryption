/*  Ritika Maknoor
  CS 1501
  Assignment 5 pt1 */



import java.util.*;
import java.io.*;
import java.math.*;

public class MyKeyGen {

    public static void main(String[] args){
        BigInteger P;
        BigInteger Q;
        BigInteger N;
        BigInteger PMinus;
        BigInteger QMinus;
        BigInteger PhiN;
        BigInteger E;
        BigInteger D;


    //First step; generate a keypair
        //Pick P and Q to be random primes; each 512 bits
        Random rnd = new Random();
        while (true){
            P = BigInteger.probablePrime(512, rnd);  
            Q = BigInteger.probablePrime(512, rnd);
        //Generate N as P*Q
            N = P.multiply(Q);                                  //returns a BigInteger whose value is (this*val)
        //Generate PHI(N) as (P-1)*(Q-1)
            PMinus = P.subtract(BigInteger.ONE);
            QMinus = Q.subtract(BigInteger.ONE);
            PhiN = PMinus.multiply(QMinus);
        //Pick E such that 1<E<PHI(N) and GCD(E, PHI(N)) = 1
        //E must not divide PHI(N) evenly
            E = new BigInteger(1024, rnd);
            while (true){
                if (E.compareTo(BigInteger.ONE) != 1){
                    if (PhiN.compareTo(E) != 1){
                        if ( (E.gcd(PhiN)).compareTo(BigInteger.ONE) != 1){
                            E = new BigInteger(1024, rnd);      //will only break out of while loop if all 3 conditions met
                            continue;
                        }
                    }
                }
                else{
                    break;
                }
            }
        //Pick D such that D = E^-1 mod PHI(N)
            if ( (E.gcd(PhiN)).equals(BigInteger.ONE)){
                D = E.modInverse(PhiN);                         
                break;
            }
        }                                                       //must be in while loop to avoid arithmetic exception with mod inverse


    //Second step
        //Save E & N to pubkey.rsa
        try{
                        // byte[] pubByteArray = BigInteger.toByteArray(E);
                        // String pubString = String(pubByteArray);

                        // File pubFile = new File("pubkey.rsa");

                        // FileWriter fw = new FileWriter(pubFile);
                        // fw.write(pubString);
                        // fw.close();


                        // PrintWriter pubkey = new PrintWriter("pubkey.rsa");
                        // pubkey.println(E);
                        // pubkey.println(N);
                        // pubkey.close();
            FileOutputStream pubFile = new FileOutputStream(new File("pubkey.rsa"));
            ObjectOutputStream pubStream = new ObjectOutputStream(pubFile);
            pubStream.writeObject(E);
            pubStream.writeObject(N);
            pubStream.close();
            pubFile.close();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }

        //Save D & N to privkey.rsa
        try{
                        // byte[] privByteArray = BigInteger.toByteArray(D);
                        // String privString = String(privByteArray);

                        // File privFile = new File("privkey.rsa");

                        // FileWriter fw = new FileWriter(privFile);
                        // fw.write(privString);
                        // fw.close();


                        // PrintWriter privkey = new PrintWriter("privkey.rsa");
                        // privkey.println(D);
                        // privkey.println(N);
                        // privkey.close();
            FileOutputStream privFile = new FileOutputStream(new File("privkey.rsa"));
            ObjectOutputStream privStream = new ObjectOutputStream(privFile);
            privStream.writeObject(D);
            privStream.writeObject(N);
            privStream.close();
            privFile.close();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }

    }
}
