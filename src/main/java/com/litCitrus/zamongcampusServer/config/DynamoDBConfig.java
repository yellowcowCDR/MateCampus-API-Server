package com.litCitrus.zamongcampusServer.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Configuration
public class DynamoDBConfig {
    private DynamoDbClient ddb;
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Bean
    public DynamoDbEnhancedClient amazonDynamoDB() {
        // DB 설정
        Region region = Region.of(amazonDynamoDBRegion);
        ddb = DynamoDbClient.builder()
                .region(region)
                .endpointOverride(URI.create(amazonDynamoDBEndpoint)) // 엔드포인트 설정
                .build();
        // 테이블 생성
        createTable();

        // Enhanced Client 생성
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
    }
    private String createTable() {
        // Chat Message Table
        String tableName = "ChatMessage";
        DynamoDbWaiter dbWaiter = ddb.waiter();

        // Primary Key 설정
        List<AttributeDefinition> attributeDefinition = new ArrayList<>();
        List<KeySchemaElement> keySchema = new ArrayList<>();
        attributeDefinition.add(AttributeDefinition.builder()
                .attributeName("roomId")
                .attributeType(ScalarAttributeType.S)
                .build());
        attributeDefinition.add(AttributeDefinition.builder()
                .attributeName("createdAt")
                .attributeType(ScalarAttributeType.S)
                .build());
        keySchema.add(KeySchemaElement.builder()
                .attributeName("roomId")
                .keyType(KeyType.HASH)
                .build());
        keySchema.add(KeySchemaElement.builder()
                .attributeName("createdAt")
                .keyType(KeyType.RANGE)
                .build());

        // TODO: ProvisionedThroughput 적절하게 설정하기
        CreateTableRequest requestChatMessageTable = CreateTableRequest.builder()
                .attributeDefinitions(
                        attributeDefinition)
                .keySchema(keySchema)
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(Long.valueOf(10))
                        .writeCapacityUnits(Long.valueOf(10))
                        .build())
                .tableName(tableName)
                .build();

        String newTable ="";

        /* 테이블이 존재한다면, 삭제하기 */
        if(isTableExist(tableName)) {
            ddb.deleteTable(DeleteTableRequest
                    .builder()
                    .tableName(tableName)
                    .build());
        }

        try {
            CreateTableResponse response = ddb.createTable(requestChatMessageTable);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            // Wait until the Amazon DynamoDB table is created
            WaiterResponse<DescribeTableResponse> waiterResponse =  dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);

            newTable = response.tableDescription().tableName();
            return newTable;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
//            System.exit(1);
        }
        return "";
    }

    /** 테이블이 존재하는지 확인 */
    private boolean isTableExist(String tableName){
        List<String> tableNames = ddb.listTables().tableNames();
        if(tableNames.contains(tableName))
            return true;
        return false;
    }
}
