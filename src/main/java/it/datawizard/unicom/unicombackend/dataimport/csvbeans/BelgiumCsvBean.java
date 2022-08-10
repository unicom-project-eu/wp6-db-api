package it.datawizard.unicom.unicombackend.dataimport.csvbeans;

import com.opencsv.bean.CsvBindByName;

public class BelgiumCsvBean extends CsvBean {
    @CsvBindByName(column = "authorization_country")
    private String authorization_country;

    @CsvBindByName(column = "vtm_name")
    private String vtm_name;

    @CsvBindByName(column = "samv2_amp_id")
    private String samv2_amp_id;

    @CsvBindByName(column = "medicinal_product_name")
    private String medicinal_product_name;

    @CsvBindByName(column = "company")
    private String company;

    @CsvBindByName(column = "amppname")
    private String amppname;

    @CsvBindByName(column = "cnk_pub")
    private String cnk_pub;

    @CsvBindByName(column = "cti_ext")
    private String cti_ext;

    @CsvBindByName(column = "abbrevname")
    private String abbrevname;

    @CsvBindByName(column = "name_abbrev_fr")
    private String name_abbrev_fr;

    @CsvBindByName(column = "packdisplayvalue")
    private String packdisplayvalue;

    @CsvBindByName(column = "espc_nl")
    private String espc_nl;

    @CsvBindByName(column = "espc_fr")
    private String espc_fr;

    @CsvBindByName(column = "marketingauthorisationholder")
    private String marketingauthorisationholder;

    @CsvBindByName(column = "distributor")
    private String distributor;

    @CsvBindByName(column = "basis_substance")
    private String basis_substance;

    @CsvBindByName(column = "substance_modifier")
    private String substance_modifier;

    @CsvBindByName(column = "formname")
    private String formname;

    @CsvBindByName(column = "edqmform")
    private String edqmform;

    @CsvBindByName(column = "edqmid")
    private String edqmid;

    @CsvBindByName(column = "snomedid")
    private String snomedid;

    @CsvBindByName(column = "abbrform_nl")
    private String abbrform_nl;

    @CsvBindByName(column = "abbrform_fr")
    private String abbrform_fr;

    @CsvBindByName(column = "manufactureddoseform")
    private String manufactureddoseform;

    @CsvBindByName(column = "adm_doseform")
    private String adm_doseform;

    @CsvBindByName(column = "roa_nl")
    private String roa_nl;

    @CsvBindByName(column = "roa_en")
    private String roa_en;

    @CsvBindByName(column = "edqm_roa_nl")
    private String edqm_roa_nl;

    @CsvBindByName(column = "edqm_roa_id")
    private String edqm_roa_id;

    @CsvBindByName(column = "baseofstrength")
    private String baseofstrength;

    @CsvBindByName(column = "strenght_nominator_value_low_limit")
    private String strenght_nominator_value_low_limit;

    @CsvBindByName(column = "strength_nominator_unit")
    private String strength_nominator_unit;

    @CsvBindByName(column = "strenght_nominator_value_high_limit")
    private String strenght_nominator_value_high_limit;

    @CsvBindByName(column = "strengthunitucum")
    private String strengthunitucum;

    @CsvBindByName(column = "strenght_denominator_value_low_limit")
    private String strenght_denominator_value_low_limit;

    @CsvBindByName(column = "strength_denominator_unit")
    private String strength_denominator_unit;

    @CsvBindByName(column = "strengthdenomunitucum")
    private String strengthdenomunitucum;

    @CsvBindByName(column = "atc")
    private String atc;

    @CsvBindByName(column = "ddd")
    private String ddd;

    @CsvBindByName(column = "ddu")
    private String ddu;

    @CsvBindByName(column = "dddpropackage")
    private String dddpropackage;

    @CsvBindByName(column = "vmp_name")
    private String vmp_name;

    @CsvBindByName(column = "vmpgroupname")
    private String vmpgroupname;
}