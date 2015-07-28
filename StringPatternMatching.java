import java.util.ArrayList;


    public static void main(String[] arg) throws  {
        String input="What is the worst-case running time of these two nested loops? The inner while\n" + 
        "loop goes around at most m times, and potentially far less when the pattern match\n" + 
        "fails. This, plus two other statements, lies within the outer for loop. The outer loop\n" + 
        "goes around at most n−m times, since no complete alignment is possible once we\n" + 
        "get too far to the right of the text. The time complexity of nested loops multiplies,\n" + 
        "so this gives a worst-case running time of O((n − m)(m + 2)).\n" + 
        "We did not count the time it takes to compute the length of the strings using\n" + 
        "the function strlen. Since the implementation of strlen is not given, we must guess\n" + 
        "how long it should take. If we explicitly count the number of characters until we";
        String searchPattern="is";
        int inptLen=input.length();
        int pattrnLen=searchPattern.length();
        ArrayList matchedIndex=new ArrayList();
        for(int i=0;i<=(inptLen-pattrnLen);i++){
            int j=0;
            while((j<pattrnLen)&& input.charAt(i+j)==searchPattern.charAt(j)){
                j++;
                if(j==pattrnLen){
                    matchedIndex.add(i);
                }
            }
        }
        System.out.println(matchedIndex);
    }
    public static ArrayList(String text, String pattern){
    }
    }
