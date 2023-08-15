package com.profiteer.core.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BankDetailsResponseTemp {

	@JsonProperty("MICR") 
    public String mICR;
    
	@JsonProperty("BRANCH") 
    public String bRANCH;
    
    @JsonProperty("ADDRESS") 
    public String aDDRESS;
    
    @JsonProperty("STATE") 
    public String sTATE;
    
    @JsonProperty("CONTACT") 
    public String cONTACT;
    
    @JsonProperty("UPI") 
    public boolean uPI;
    
    @JsonProperty("RTGS") 
    public boolean rTGS;
    
    @JsonProperty("CITY") 
    public String cITY;
    
    @JsonProperty("CENTRE") 
    public String cENTRE;
    
    @JsonProperty("DISTRICT") 
    public String dISTRICT;
    
    @JsonProperty("NEFT") 
    public boolean nEFT;
    
    @JsonProperty("IMPS") 
    public boolean iMPS;
    
    @JsonProperty("SWIFT") 
    public Object sWIFT;
    
    @JsonProperty("ISO3166") 
    public String iSO3166;
    
    @JsonProperty("BANK") 
    public String bANK;
    
    @JsonProperty("BANKCODE") 
    public String bANKCODE;
    
    @JsonProperty("IFSC") 
    public String iFSC;
}
