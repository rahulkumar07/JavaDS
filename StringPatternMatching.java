/*
This is just a implement of algo suggested by 2.5.3 String Pattern Matching- Skiena
*/

import java.util.ArrayList;


    public static void main(String[] arg) throws  {
        matchPattern("asjfa kdfhak jdfhkjadkj kadfka ", "fa");
    }
    public static ArrayList matchPattern(String text, String pattern){
         String input=text;
        String searchPattern=pattern;
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
        return matchedIndex;
    }
    
    }
