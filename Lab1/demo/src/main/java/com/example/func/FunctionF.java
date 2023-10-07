package com.example.func;

public class FunctionF extends Function{


    public FunctionF(String input, int minorErrorAttempts){
        super(input, minorErrorAttempts);
    }

    @Override
    public Result compute(){
        Result res = new Result();
        int val = 0;
        try{
            val = Integer.parseInt(input);
        }catch(NumberFormatException exception){
            res.status = fatal_error_msg;
            return res;
        }
        if(val < 0 ){
            for (int i = 0; i < minorErrorAttempts; i++) {
                int flip = rnd.nextInt(2);
                if(flip == 0){
                    continue;
                }else{
                    val = (int)Math.ceil(Math.sqrt((double)val))
                }
            }
        }
        res.value = val;
        res.status = success_msg;
        return res;
    }
}
