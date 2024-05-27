package codehows.dream.dreambulider.dto.HashTag;

public class MemberNoExistExcpetion extends RuntimeException{
    public MemberNoExistExcpetion(String message){
        super(message);
    }

    public MemberNoExistExcpetion(){
        super();
    }
}
