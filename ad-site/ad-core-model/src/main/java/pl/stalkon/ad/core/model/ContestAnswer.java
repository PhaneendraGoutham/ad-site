package pl.stalkon.ad.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "contest_answer", uniqueConstraints={
		@UniqueConstraint(columnNames={"user_id", "contest_id"})
})
public class ContestAnswer extends CommonEntity {

	private static final long serialVersionUID = 7251846403172177384L;

	private String answer;
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contest_id")
	private Contest contest;

	@Column(nullable=false)
	private Boolean winner = false;
	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}
	
	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
