package com.example.parkinson.data.enums;

/** An enum class of all child tables names in User DatabaseReference **/
public enum EDataSourceUser {
    USER_DETAILS("user_details"),
    QUESTIONNAIRE("questionnaire"),
    INDICES_LIST("indices_list"),
    REPORTS("reports");

    public final String name;
    private EDataSourceUser(String name) {
        this.name = name;
    }
}
