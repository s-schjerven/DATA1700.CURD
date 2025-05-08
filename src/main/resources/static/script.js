document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded");

    let errorMessage = document.getElementById("errorMessage");
    let submitButton = document.getElementById("submit");

    if (submitButton) {
        submitButton.addEventListener("click", enterNew)
    }
    async function enterNew(e){
        e.preventDefault();

        let firstNameInput = document.getElementById("firstName").value.trim();
        let lastNameInput = document.getElementById("lastName").value.trim();
        let phoneNumberInput = document.getElementById("phoneNumber").value.trim();

        errorMessage.innerHTML = "";

        if (!firstNameInput || !lastNameInput || !phoneNumberInput) {
            errorMessage.innerHTML = "Please fill all fields";
        }

        try {
            const response = await fetch("http://localhost:8080/api/employees/post", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    firstName: firstNameInput,
                    lastName: lastNameInput,
                    phoneNumber: phoneNumberInput,
                })
            })
            if (response.ok) {
                const newEmployee = await response.json();

                document.getElementById("loginSite").reset();
                appendEmployeeRow(newEmployee);

            } else {
                errorMessage.innerHTML = "Server error. Please try again.";
            }
        } catch (error) {
            console.error(error);
            errorMessage.innerHTML = "Network error. Please check console log.";
        }
    }

    async function loadEmployees() {
        try {
            const response = await fetch("http://localhost:8080/api/employees/all");
            if (!response.ok) {
                errorMessage.innerHTML = "Server error. Please check console log.";
            }
            const employees = await response.json();
            const tableBody = document.getElementById("tableBody");
            tableBody.innerHTML = "";

            employees.forEach(appendEmployeeRow);

        } catch (error) {
            console.error(error);
            errorMessage.innerHTML = "Loading error. Please check console log.";
        }
    }
    let generateList = document.getElementById("generate");
    generateList.addEventListener("click", loadEmployees);
    let saveButton = document.getElementById("save");
    saveButton.addEventListener("click", updateEmployee);

    let editButton = document.getElementById("edit");
    editButton.addEventListener('click', showEditForm);
    let newEmployeeFrom = document.getElementById("closeEdit");
    newEmployeeFrom.addEventListener("click", closeEditForm);


    async function updateEmployee(e){
        e.preventDefault();

        let errorMessageEdit = document.getElementById("errorMessageEdit");
        let id = document.getElementById("idEdit").value.trim();
        let firstNameEdit = document.getElementById("firstNameEdit").value.trim();
        let lastNameEdit = document.getElementById("lastNameEdit").value.trim();
        let phoneNumberEdit = document.getElementById("phoneNumberEdit").value.trim();

        errorMessageEdit.innerHTML = "";

        if (!id || !firstNameEdit || !lastNameEdit || !phoneNumberEdit) {
            errorMessageEdit.innerHTML = "All fields are required.";
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/employees/put/${id}`,{
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    firstName: firstNameEdit,
                    lastName: lastNameEdit,
                    phoneNumber: phoneNumberEdit,
                })
            });
            document.getElementById("editSite").reset();

            if (!response.ok) {
                errorMessageEdit.innerHTML = "Failed to edit employee info. Please check console log.";
            }

            await loadEmployees();

        } catch (error) {
            console.error(error);
        }

    }
})

function appendEmployeeRow(employee) {
    const tableBody = document.getElementById("tableBody");
    const row = document.createElement("tr");

    row.innerHTML = `
        <td>${employee.firstName}</td>
        <td>${employee.lastName}</td>
        <td>${employee.phoneNumber}</td>
        <td><button class="btn btn-danger btn-sm" data-id="${employee.id}">Delete</button></td> 
    `;

    row.querySelectorAll(".btn").forEach(button => {
        button.addEventListener("click", async function(){
            const id = this.getAttribute("data-id");
            const response = await fetch(`http://localhost:8080/api/employees/${id}`,{
                method: "DELETE"
            });
            if (response.ok) {
                this.closest("tr").remove();
            } else {
                console.log("Could not delete employee!")
            }
        })
    })
    tableBody.appendChild(row);
}

function showEditForm() {
    document.getElementById("editPage").classList.remove("hidden");
    document.getElementById("loginPage").classList.add("hidden");
    document.getElementById("editButtonContainer").classList.add("hidden");
    document.getElementById("closeEditButtonContainer").classList.remove("hidden");
}

function closeEditForm() {
    document.getElementById("editPage").classList.add("hidden");
    document.getElementById("loginPage").classList.remove("hidden");
    document.getElementById("editButtonContainer").classList.remove("hidden");
    document.getElementById("closeEditButtonContainer").classList.add("hidden");
}

