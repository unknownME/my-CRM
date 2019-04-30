package application;

public class Client {

	private String firstName;
	private String lastName;
	private String patronymic;
	private String age;
	private String activity;
	private String massagetype;
	private String diseases;
	private String recommendations;

	public Client(String firstName, String lastName, String patronymic, String age, String activity, String massagetype,
			String diseases, String recommendations) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
		this.age = age;
		this.activity = activity;
		this.massagetype = massagetype;
		this.diseases = diseases;
		this.recommendations = recommendations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getMassagetype() {
		return massagetype;
	}

	public void setMassagetype(String massagetype) {
		this.massagetype = massagetype;
	}

	public String getDiseases() {
		return diseases;
	}

	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}

	public String getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}

}
