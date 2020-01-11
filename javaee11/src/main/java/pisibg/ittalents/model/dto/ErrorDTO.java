package pisibg.ittalents.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ErrorDTO {
    //TODO Exception handlng, Abstract Controller?
    private String msg;
    private int status;
    private LocalDateTime time;
    private String exceptionType;
}
