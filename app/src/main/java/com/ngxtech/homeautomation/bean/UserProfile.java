package com.ngxtech.homeautomation.bean;

public class UserProfile {

        private String _id;
        private String userName;
        private String firstName;
        private String lastName;
        private String businessName;
        private String mobile;
        private String email;
        private String profile_pic;
        private String address;
        private String pin;
        private String state;
        private String city;
        private String country;
        private String imageUrl;


        public void setUserName(String userName){
            this.userName = userName;
        }
        public void setFirstName(String firstName){
            this.firstName = firstName;
        }
        public void setLastName(String lastName){
            this.lastName = lastName;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public void setPin(String pin){
            this.pin = pin;
        }
        public void setState(String state){
            this.state = state;
        }
        public void setBusinessName(String businessName){
            this.businessName = businessName;
        }
        public void setCity(String city){
            this.city = city;
        }
        public void setCountry(String country){
            this.country = country;
        }
        public void setProfile_pic(String profile_pic){
            this.profile_pic = profile_pic;
        }
        public void setImageUrl(String imageUrl){
            this.imageUrl = imageUrl;
        }
        public String getUserName(){
            return this.userName;
        }
        public String getBusinessName(){
            return this.businessName;
        }
        public String getFirstName(){
            return this.firstName;
        }
        public String getLastName(){
            return this.lastName;
        }
        public String getMobile(){
            return this.mobile;
        }
        public String getEmail(){
            return this.email;
        }
        public String getAddress(){
            return this.address;
        }
        public String getPin(){
            return this.pin;
        }
        public String getState(){
            return this.state;
        }
        public String getCountry(){
            return this.country;
        }
        public String getCity(){
            return this.city;
        }
        public String getProfile_pic(){
            return this.profile_pic;
        }
        public String getImageUrl(){
            return this.imageUrl;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }



}
