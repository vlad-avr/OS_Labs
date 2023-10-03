package com.example.func;

public class FunctionG extends Function{
    public FunctionG(String input){
        super(input);
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
        /*IMPLEMENT FUNCTION LATER */
        res.value = val;
        res.status = success_msg;
        return res;
    }
}
