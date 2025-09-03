package fun.xianlai.admax.modules.system.initializer;

import fun.xianlai.admax.modules.system.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Component
public class CacheInitializer implements CommandLineRunner {
    @Autowired
    private OptionService optionService;

    @Override
    public void run(String... args) throws Exception {
        optionService.updateFrontLoadOptionsCache();
        optionService.updateBackLoadOptionCache();
    }
}
