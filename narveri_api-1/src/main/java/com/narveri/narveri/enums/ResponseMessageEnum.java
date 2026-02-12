package com.narveri.narveri.enums;

public enum ResponseMessageEnum {




    BACK_SYSTEM_ERROR_MSG_001("back.system.error.msg.001", "Sistemsel bir hata alınmıştır, Daha sonra tekrar deneyiniz."),

    BACK_JSON_CONVERTOR_MSG_001("back.user_convertor.msg.001", "Json convertor yaparken hata alinmistir."),


    BACK_ROLE_MSG_001("back.role.msg.001", "Rol Tanımı bulunamamıştır."),
    BACK_ROLE_MSG_002("back.role.msg.002", "Bu isimde rol tanımı var, farklı bir isim giriniz."),
    BACK_ROLE_MSG_003("back.role.msg.003", "Rolü, eklenmiş olan kullanıcılardan çıkardıktan sonra silebilirsiniz."),



    BACK_PRIVILEGE_MSG_001("back.privilege.msg.001", "Yetki Tanımı bulunamamıştır."),
    BACK_PRIVILEGE_MSG_002("back.privilege.msg.002", "Yetkiyi Eklenmiş olan rollerden çıkardıktan sonra silebilirsiniz."),
    BACK_PRIVILEGE_MSG_003("back.privilege.msg.003", "Bu isimde yetki tanımı var, farklı bir isim giriniz."),



    BACK_PARAMETER_MSG_001("back.parameter.msg.001", "Parametre bulunamadı."),
    BACK_PARAMETER_MSG_002("back.parameter.msg.002", "Parameter başarılı bir şekilde güncellendi."),
    BACK_PARAMETER_MSG_003("back.parameter.msg.003", "Parameter başarılı bir şekilde oluşturuldu ya da zaten var."),
    BACK_PARAMETER_MSG_004("back.parameter.msg.004", "Parameter başarılı bir şekilde silinmiştir."),
    BACK_PARAMETER_MSG_005("back.parameter.msg.005", "Parametre zaten kayıtlı."),




    BACK_USER_MSG_001("back.user.msg.001", "Kullanıcı bulunamamıştır."),
    BACK_USER_MSG_002("back.user.msg.002", "Bu Telefon Sistemde Kayıtlıdır."),
    BACK_USER_MSG_003("back.user.msg.003", "Bu Mail Sistemde Kayıtlıdır."),
    BACK_USER_MSG_004("back.user.msg.004", "Bu kullanıcı aktif değildir."),
    BACK_USER_MSG_007("back.user.msg.007", "Email veya şifre hatalı."),
    BACK_USER_MSG_006("back.user.msg.006", " Telefon veya email zaten kullanımda"),
    BACK_USER_MSG_008("back.user.msg.008", " sdlkaafdlska;klffdsklfd;kl"),
    BACK_USER_MSG_009("back.user.msg.009", " Bu işlemi sadece şu anki kullanıcı yapabilir. "),



    BACK_VALIDATION_MSG_001("back.validation.msg.001", "Kullanıcı bilgileri hatalı (TC - AD - SOYAD - YIL)"),


    BACK_FILE_MSG_001("back.file.msg.001", "Dosya başarıyla yüklendi."),
    BACK_FILE_MSG_002("back.file.msg.002", "Dosya yüklenirken bir hata oluştu."),
    BACK_FILE_MSG_003("back.file.msg.003", "Dosya başarıyla silindi."),
    BACK_FILE_MSG_004("back.file.msg.004", "Dosya silinirken bir hata oluştu."),
    BACK_FILE_MSG_005("back.file.msg.005", "PDF metni başarıyla çıkarıldı."),
    BACK_FILE_MSG_006("back.file.msg.006", "PDF metni çıkarılamadı."),

    BACK_RAGDOCUMENT_MSG_001("back.rag.document.msg.001", "Dosya boş olmaması gerekiyor."),
    BACK_RAGDOCUMENT_MSG_002("back.rag.document.msg.002", "Text boş olmaması gerekiyor."),
    BACK_RAGDOCUMENT_MSG_003("back.rag.document.msg.003", "Döküman bulunamadı"),


    ;


    private final String message;
    private final String messageDetail;

    ResponseMessageEnum(String message, String messageDetail) {
        this.message = message;
        this.messageDetail = messageDetail;
    }

    public String message() {
        return this.message;
    }

    public String messageDetail() {
        return this.messageDetail;
    }
}
