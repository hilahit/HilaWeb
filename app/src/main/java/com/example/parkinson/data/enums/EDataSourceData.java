package com.example.parkinson.data.enums;

/** An enum class of all child tables names in Data DatabaseReference **/
public enum EDataSourceData {
    QUESTIONNAIRE_NEW_PATIENT("questionnaire_new_patient"),
    QUESTIONNAIRE_FOLLOW_UP("questionnaire_follow_up"),
    INDICES_LIST("indices_list");

    public final String name;
    private EDataSourceData(String name) {
        this.name = name;
    }
}
