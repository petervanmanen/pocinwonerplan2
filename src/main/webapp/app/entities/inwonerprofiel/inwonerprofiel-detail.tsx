import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inwonerprofiel.reducer';

export const InwonerprofielDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inwonerprofielEntity = useAppSelector(state => state.inwonerprofiel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inwonerprofielDetailsHeading">Inwonerprofiel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{inwonerprofielEntity.id}</dd>
          <dt>
            <span id="voornaam">Voornaam</span>
          </dt>
          <dd>{inwonerprofielEntity.voornaam}</dd>
          <dt>
            <span id="tussenvoegsel">Tussenvoegsel</span>
          </dt>
          <dd>{inwonerprofielEntity.tussenvoegsel}</dd>
          <dt>
            <span id="achternaam">Achternaam</span>
          </dt>
          <dd>{inwonerprofielEntity.achternaam}</dd>
          <dt>
            <span id="geboortedatum">Geboortedatum</span>
          </dt>
          <dd>
            {inwonerprofielEntity.geboortedatum ? (
              <TextFormat value={inwonerprofielEntity.geboortedatum} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="bsn">Bsn</span>
          </dt>
          <dd>{inwonerprofielEntity.bsn}</dd>
          <dt>Inwonerplan</dt>
          <dd>{inwonerprofielEntity.inwonerplan ? inwonerprofielEntity.inwonerplan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/inwonerprofiel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inwonerprofiel/${inwonerprofielEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InwonerprofielDetail;
