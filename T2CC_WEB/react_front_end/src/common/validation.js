
export function EmailValidation(email) {
  const EmailRegrex = /[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}/igm;
  if (email == '' || !EmailRegrex.test(email)) {
    return false;
  } return true;
}

export function PhoneNumberValidation(phoneno) {
  const PhoneNoRegrex = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
  if (phoneno == '' || !PhoneNoRegrex.test(phoneno)) {
    return false;
  } return true;
}
