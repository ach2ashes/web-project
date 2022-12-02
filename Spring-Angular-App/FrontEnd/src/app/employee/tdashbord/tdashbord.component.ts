import { Component, OnInit } from '@angular/core';
import { Company } from 'src/app/model/company';
import { User } from 'src/app/model/user';
import { EmployeeService } from 'src/app/service/techerService/employee.service';
import { ModalService } from '../modal/modal.service';

@Component({
  selector: 'app-tdashbord',
  templateUrl: './tdashbord.component.html',
  styleUrls: ['./tdashbord.component.css'],
})
export class TdashbordComponent implements OnInit {
  companyId = 0;
  companyName: string = '';
  companyDescription: string = '';
  openDate: string = '';
  closeDate: string = '';
  companiesByEmployee: Company[] = [];
  companyToDelete: number = 0;
  company: Company = new Company();
  employee: User = new User(
    parseInt(localStorage.getItem('userId')!),
    localStorage.getItem('full-name')!,
    localStorage.getItem('token')!
  );

  constructor(
    private employeeService: EmployeeService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.employeeService.getCompaniesByEmployee(this.employee.id).subscribe(
      (res) => {
        this.companiesByEmployee = res;
        this.company = this.companiesByEmployee[0];
      },
      (err) => {
        console.log(err);
      }
    );
  }

  deleteCompany(id: number) {
    this.companyToDelete = id;
    this.employeeService.deleteCompany(id).subscribe(
      (res) => {
        this.companiesByEmployee = this.companiesByEmployee.filter(
          (company) => company.id != id
        );
      },
      (err) => {
        console.log(err);
        this.modalService.open('modal4');
      }
    );
  }
  forcedeleteCompany() {
    this.employeeService.forcedeleteCompany(this.companyToDelete).subscribe(
      (res) => {
        this.companiesByEmployee = this.companiesByEmployee.filter(
          (company) => company.id != this.companyToDelete
        );
      },
      (err) => {
        console.log(err);
      }
    );
    this.closeModal('modal4');
  }

  openModal(id: string, idCompany: number) {
    this.cleanFields();
    this.modalService.open(id);
    this.company = this.companiesByEmployee.filter(
      (company) => company.id == idCompany
    )[0];
  }

  closeModal(id: string) {
    this.modalService.close(id);
  }

  addCompany(id: string) {
    let company = new Company();
    company.nom = this.companyName;
    company.description = this.companyDescription;
    company.openDate = this.openDate;
    company.closeDate = this.closeDate;
    company.ownerId = this.employee.id;
    if (
      company.nom == '' ||
      company.description == '' ||
      company.openDate == '' ||
      company.closeDate == ''
    ) {
      alert('Please fill the fields');
    } else {
      let opdate = new Date(company.openDate);
      let cldate = new Date(company.closeDate);
      if (opdate > cldate) {
        alert('close date must be greater than open date');
      } else {
        console.log(company.openDate);
        this.employeeService.addCompany(company).subscribe(
          (res) => {
            this.companiesByEmployee.push(res);
            this.closeModal(id);
          },
          (err) => {
            console.log(err);
          }
        );
      }
    }
  }

  showCompanyWithDetails(id: string, companyId: number) {
    this.company = this.companiesByEmployee.filter(
      (company) => company.id == companyId
    )[0];
    this.companyName = this.company.nom;
    this.companyId = companyId;
    this.companyDescription = this.company.description;
    this.openDate = this.company.openDate;
    this.closeDate = this.company.closeDate;
    this.modalService.open(id);
  }

  editCompany(modalId: string): void {
    let company: Company = new Company();
    company.id = this.companyId;
    company.nom = this.companyName;
    company.description = this.companyDescription;
    company.openDate = this.openDate;
    company.closeDate = this.closeDate;
    if (
      company.nom == '' ||
      company.description == '' ||
      company.openDate == '' ||
      company.closeDate == ''
    ) {
      alert('Please fill the fields');
    } else {
      let opdate = new Date(company.openDate);
      let cldate = new Date(company.closeDate);
      if (opdate > cldate) {
        alert('close date must be greater than open date');
      } else {
        this.employeeService.updateCompany(company).subscribe(
          (res) => {
            console.log(res);
            let index = 0;
            index = this.companiesByEmployee.findIndex((company) => {
              return company.id == Number(res.id);
            });
            this.companiesByEmployee[index] = res;
            console.log(index);
          },
          (err) => {
            console.log(err);
          }
        );
        this.closeModal(modalId);
      }
    }
  }

  cleanFields() {
    //this.companyId = 0;
    this.companyName = '';
    this.companyDescription = '';
    this.openDate = '';
    this.closeDate = '';
  }
}
