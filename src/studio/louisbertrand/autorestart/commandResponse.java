package studio.louisbertrand.autorestart;

/**
 * commandResponse
 * 
 * simple response holder
 */
public class commandResponse {

    private String message;
    private Integer code;
    
    public String getMessage() {
    	return this.message;
    }
    public Integer getCode() {
    	return this.code;
    }
    
    public boolean setMessage(String variable) {
    	
    	if (variable == null) {
    		
    	}
    	return true;
    	
    }
    
    public boolean setCode(Integer variable) {
    	
    	if (variable == null) {
    		
    	}
    	return true;
    	
    }

}
