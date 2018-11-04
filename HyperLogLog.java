

import java.util.Scanner;

public class HyperLogLog {

    public static void main(String[] args){


        int hashMatrix[] = {0x21ae4036, 0x32435171, 0xac3338cf, 0xea97b40c, 0x0e504b22, 0x9ff9a4ef
                , 0x111d014d, 0x934f3787, 0x6cd079bf, 0x69db5c31, 0xdf3c28ed
                , 0x40daf2ad, 0x82a5891c, 0x4659c7b0, 0x73dc0ca8
                , 0xdad3aca2, 0x00c74c7e, 0x9a2521e2, 0xf38eb6aa
                , 0x64711ab6, 0x5823150a, 0xd13a3a9a, 0x30a5aa04
                , 0x0fb9a1da, 0xef785119, 0xc9f0b067, 0x1e7dde42
                , 0xdda4a7b2, 0x1a1c2640, 0x297c0633, 0x744edb48, 0x19adce93};


        Scanner scanner = new Scanner(System.in);
        int threshold = Integer.parseInt(scanner.next());

        // size by choice
        int [] M = new int[1024];

        while (scanner.hasNext()) MappingInputData(Integer.parseInt(scanner.next()), hashMatrix, M);

        thresholdVSestimate(threshold, estimateDistinctElements(M));
    }


    private static void MappingInputData (int input, int [] matrix, int [] M){

        int j = hashDistribution(input);
        M[j] = Math.max(M[j], Integer.numberOfLeadingZeros(hashRange(input, matrix)));
    }


    /**
     * hashing input random and uniformly
     * @param input Integer of input stream
     * @return index of M
     */
    private static int hashDistribution(int input){

        return ((input*0xbc164501) & 0x7fe00000) >>21;
    }


    private static double estimateDistinctElements(int [] M){

        int V = M.length;
        double Z = 0;

        for (int k = 0; k < M.length; k++){
            if (M[k]!=0) V --;
            Z+= (2^(-M[k]));
        }

        double q = (double)1/ Z;
        double estimate = (M.length*M.length*q*0.7213) / ((double)1+ (1.079/(double)M.length));

        if (estimate < 2.5 * M.length && V > 0) estimate = M.length * Math.log((double)M.length/(double)V);

        return estimate;
    }


    /**
     * hashing within range of M.length by applying bit AND operation to input integer and each row of the hashMatrix.
     * @param input integer of input stream
     * @param matrix hash matrix
     * @return index
     */
    private static int hashRange(int input, int[] matrix){

        int result = 0;

        for (int i = 0; i < 32; i++){
            int subResult = Long.bitCount(matrix[i] & input) % 2;
            result = (result<<1) + subResult;
        }

        return Math.abs(result);
    }


    private static void thresholdVSestimate (int threshold, double E){

        if (threshold < E) System.out.println("above");
        if (E < threshold) System.out.println("below");
    }

}
