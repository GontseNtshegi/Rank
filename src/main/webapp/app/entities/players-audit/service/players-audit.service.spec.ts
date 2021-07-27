import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlayersAudit, PlayersAudit } from '../players-audit.model';

import { PlayersAuditService } from './players-audit.service';

describe('Service Tests', () => {
  describe('PlayersAudit Service', () => {
    let service: PlayersAuditService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlayersAudit;
    let expectedResult: IPlayersAudit | IPlayersAudit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlayersAuditService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        eventDate: currentDate,
        operation: 'AAAAAAA',
        winningMoney: 0,
        transactionId: 'AAAAAAA',
        playerId: 0,
        promotion: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            eventDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PlayersAudit', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            eventDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            eventDate: currentDate,
          },
          returnedFromService
        );

        service.create(new PlayersAudit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PlayersAudit', () => {
        const returnedFromService = Object.assign(
          {
            eventDate: currentDate.format(DATE_TIME_FORMAT),
            operation: 'BBBBBB',
            winningMoney: 1,
            transactionId: 'BBBBBB',
            playerId: 1,
            promotion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            eventDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PlayersAudit', () => {
        const patchObject = Object.assign(
          {
            eventDate: currentDate.format(DATE_TIME_FORMAT),
            operation: 'BBBBBB',
            winningMoney: 1,
          },
          new PlayersAudit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            eventDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PlayersAudit', () => {
        const returnedFromService = Object.assign(
          {
            eventDate: currentDate.format(DATE_TIME_FORMAT),
            operation: 'BBBBBB',
            winningMoney: 1,
            transactionId: 'BBBBBB',
            playerId: 1,
            promotion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            eventDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PlayersAudit', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlayersAuditToCollectionIfMissing', () => {
        it('should add a PlayersAudit to an empty array', () => {
          const playersAudit: IPlayersAudit = { transactionId: 'ABC' };
          expectedResult = service.addPlayersAuditToCollectionIfMissing([], playersAudit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playersAudit);
        });

        it('should not add a PlayersAudit to an array that contains it', () => {
          const playersAudit: IPlayersAudit = { transactionId: 'ABC' };
          const playersAuditCollection: IPlayersAudit[] = [
            {
              ...playersAudit,
            },
            { transactionId: 'CBA' },
          ];
          expectedResult = service.addPlayersAuditToCollectionIfMissing(playersAuditCollection, playersAudit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PlayersAudit to an array that doesn't contain it", () => {
          const playersAudit: IPlayersAudit = { transactionId: 'ABC' };
          const playersAuditCollection: IPlayersAudit[] = [{ transactionId: 'CBA' }];
          expectedResult = service.addPlayersAuditToCollectionIfMissing(playersAuditCollection, playersAudit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playersAudit);
        });

        it('should add only unique PlayersAudit to an array', () => {
          const playersAuditArray: IPlayersAudit[] = [
            { transactionId: 'ABC' },
            { transactionId: 'CBA' },
            { transactionId: 'invoice Utah' },
          ];
          const playersAuditCollection: IPlayersAudit[] = [{ transactionId: 'ABC' }];
          expectedResult = service.addPlayersAuditToCollectionIfMissing(playersAuditCollection, ...playersAuditArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const playersAudit: IPlayersAudit = { transactionId: 'ABC' };
          const playersAudit2: IPlayersAudit = { transactionId: 'CBA' };
          expectedResult = service.addPlayersAuditToCollectionIfMissing([], playersAudit, playersAudit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(playersAudit);
          expect(expectedResult).toContain(playersAudit2);
        });

        it('should accept null and undefined values', () => {
          const playersAudit: IPlayersAudit = { transactionId: 'ABC' };
          expectedResult = service.addPlayersAuditToCollectionIfMissing([], null, playersAudit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(playersAudit);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
