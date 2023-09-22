package com.example.csv.config;

import com.example.csv.core.entities.Lead;
import com.example.csv.core.entities.TempLead;
import com.example.csv.core.dto.TempLeadDto;
import com.example.csv.util.BatchConstants;
import com.example.csv.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class SpringBatchConfig {


    private static final String[] LEAD_TOKEN = new String[]{
            "Email",
            "Phone"
    };


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public TaskExecutor threadPoolTaskExecutorLead() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(12);
        executor.setCorePoolSize(8);
        executor.setQueueCapacity(15);
        executor.afterPropertiesSet();

        return executor;
    }


    @Bean
    public JobLauncher asyncJobLauncherLead() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(threadPoolTaskExecutorLead());
        return jobLauncher;
    }


    /* LEAD JOB */
    @Bean
    public Job leadJob(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       ItemReader<TempLeadDto> leadFileItemReader,
                       ItemProcessor<TempLeadDto, TempLead> leadItemProcessor,
                       ItemWriter<TempLead> leadItemWriter,
                       ItemReader<TempLead> leadItemReader,
                       @Qualifier(BatchConstants.LEAD_PROCESSOR) ItemProcessor<TempLead, Lead> itemProcessor,
                       @Qualifier(BatchConstants.LEAD_WRITTER) ItemWriter<Lead> itemWriter) {

        log.debug("LEAD JOB starting...");

        Step stepLoadFile = stepBuilderFactory.get(BatchConstants.LEAD_STEP_LOAD_FILE)
                .<TempLeadDto, TempLead>chunk(Constants.CHUNK_SIZE)
                .reader(leadFileItemReader)
                .processor(leadItemProcessor)
                .writer(leadItemWriter)
                .build();

        Step stepProcessDb = stepBuilderFactory.get(BatchConstants.LEAD_STEP_PROCESS_DB)
                .<TempLead, Lead>chunk(1)
                .reader(leadItemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        log.info("Step started...");
        return jobBuilderFactory.get(BatchConstants.LEAD_JOB)
                .incrementer(new RunIdIncrementer())
                .start(stepLoadFile)
                .next(stepProcessDb)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TempLeadDto> leadFileItemReader(@Value("#{jobParameters[csvPath]}") String pathToFile) {

        try {

            log.info("Reading flat file lead....................................................................................");
            FlatFileItemReader<TempLeadDto> flatFileItemReader = new FlatFileItemReader<>();
            flatFileItemReader.setResource(new FileSystemResource(pathToFile));

            flatFileItemReader.setName("CSV-Reader");
            flatFileItemReader.setLinesToSkip(1); //skip csv head

            flatFileItemReader.setLineMapper(lineMapperLead());

            return flatFileItemReader;
        } catch (Exception x) {
            log.error("Reading flat file lead error.............................................................................");
            throw x;
        }
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<TempLead> leadItemReader(@Value("#{stepExecution}") StepExecution stepExecution) {

        JpaPagingItemReader<TempLead> reader = new JpaPagingItemReader<TempLead>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT c FROM TempLead c WHERE c.jobId = :jobId");

        log.info("reading...............................................................................");
        Long jobId = stepExecution.getJobExecution().getJobInstance().getInstanceId();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobId", Integer.parseInt(jobId.toString()));
        reader.setParameterValues(parameters);
        reader.setSaveState(false);
        reader.setPageSize(1000);

        return reader;

    }


    @Bean
    public LineMapper<TempLeadDto> lineMapperLead() {

        DefaultLineMapper<TempLeadDto> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(LEAD_TOKEN);

        BeanWrapperFieldSetMapper<TempLeadDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(TempLeadDto.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    /******************** end lead **************************/


    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}
