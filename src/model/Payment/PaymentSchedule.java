package model.Payment;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.UUID;

public class PaymentSchedule implements Serializable 
{
    private UUID id;
    private int type;   
    private int day;    
    private int frequency;  
    private int weekDay;    

    public PaymentSchedule(int type, int day) 
    {
        this.type = type;
        this.day = day;
        this.id = UUID.randomUUID();
    }

    public PaymentSchedule(int type, int frequency, int weekDay) 
    {
        this.type = type;
        this.frequency = frequency;
        this.weekDay = weekDay;
        this.id = UUID.randomUUID();
    }

    public int getType() 
    {
        return type;
    }

    public void setType(int type) 
    {
        this.type = type;
    }

    public int getDay() 
    {
        return day;
    }

    public void setDay(int day) 
    {
        this.day = day;
    }

    public int getFrequency() 
    {
        return frequency;
    }

    public void setFrequency(int frequency) 
    {
        this.frequency = frequency;
    }

    public int getWeekDay() 
    {
        return weekDay;
    }

    public void setWeekDay(int weekDay) 
    {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        String typeString = "";
        if (type == 1) 
        {
            if (day == 0) 
            {
                typeString = "Monthly, Day: last month day";
            } 
            else 
            {
                typeString = "Monthly, Day: " + day;
            }
        } 
        else if (type == 2) 
        {
            typeString = "Weekly, frequency payment: " +
                    frequency + "week(s)" +
            ", Week day payment: " + DayOfWeek.of(weekDay);
        }
        return "Payment schedule {" +
                "Type: " + typeString +
                '}';
    }
}
