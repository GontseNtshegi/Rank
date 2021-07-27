jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PlayersAuditService } from '../service/players-audit.service';

import { PlayersAuditDeleteDialogComponent } from './players-audit-delete-dialog.component';

describe('Component Tests', () => {
  describe('PlayersAudit Management Delete Component', () => {
    let comp: PlayersAuditDeleteDialogComponent;
    let fixture: ComponentFixture<PlayersAuditDeleteDialogComponent>;
    let service: PlayersAuditService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayersAuditDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PlayersAuditDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayersAuditDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PlayersAuditService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('ABC');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('ABC');
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
