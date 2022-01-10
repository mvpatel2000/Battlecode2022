import java.lang.Math;
import java.lang.StringBuilder; 
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class GenerateSwitch {
    public static void main(String args[]) {
    
        String out = "";
        
        /**
        for(int start = 0; start<32; start++) {
            for(int end = start; end<32; end++) {
                out += bitmask(start, end);  
            }
         }
         */
        for(int index = 0; index<64; index++) {
            System.out.println(String.valueOf(index));
            for(int bitloc = 16*index; bitloc<16*(index+1); bitloc++) {
                out += whichBit(index, bitloc);  
            }
         }
         System.out.println(out);
         try {
             FileWriter myWriter = new FileWriter("switch.txt");
             myWriter.write(out);
             myWriter.close();
             System.out.println("wrote to file");
         } catch (IOException e) {
             System.out.println("An error occurred.");
             e.printStackTrace();
         }
    }
            
    public static String bitmask(int start, int end) {
        int mask = 32*start + end;
        
        int SHARED_ARRAY_ELEM_SIZE = 16;
        start += 32 - SHARED_ARRAY_ELEM_SIZE;
        end += 32 - SHARED_ARRAY_ELEM_SIZE;
        int bitmask = -1;
        if(end == 31) {
            bitmask = 0;
        } else {
            bitmask = bitmask >>> (end+1);
        }
        int bitmask2 = -1;
        bitmask2 = bitmask2 >>> (start);
        //for reading
        int read = bitmask ^ bitmask2;
        //for writing
        int write = ~(bitmask ^ bitmask2);
        
        String out = "case " + Integer.toString(mask) + ": " + " if(read) {return " + Integer.toString(read) + ";} else {return " + Integer.toString(write) +";} \n";
        
        return out;
    }

    public static String whichBit(int index, int bitloc) {
        int mask = bitloc + index*2000;
        int SHARED_ARRAY_ELEM_SIZE = 16;
        int binary = 0;
        if(bitloc < index * SHARED_ARRAY_ELEM_SIZE) {
            binary = 0;    //first bit of the number
        }
        if(bitloc >= (index+1)*SHARED_ARRAY_ELEM_SIZE) {
            binary = SHARED_ARRAY_ELEM_SIZE-1;  //last bit of the number
        }
        binary = bitloc % SHARED_ARRAY_ELEM_SIZE;
        
        String out = "case " + Integer.toString(mask) + ": " + "return " + Integer.toString(binary) +";\n";
        
        return out;
    }
}