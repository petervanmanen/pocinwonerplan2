import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subdoel.reducer';

export const SubdoelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subdoelEntity = useAppSelector(state => state.subdoel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subdoelDetailsHeading">Subdoel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{subdoelEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{subdoelEntity.code}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{subdoelEntity.naam}</dd>
          <dt>
            <span id="actief">Actief</span>
          </dt>
          <dd>{subdoelEntity.actief ? 'true' : 'false'}</dd>
          <dt>Aandachtspunt</dt>
          <dd>{subdoelEntity.aandachtspunt ? subdoelEntity.aandachtspunt.id : ''}</dd>
          <dt>Ontwikkelwens</dt>
          <dd>{subdoelEntity.ontwikkelwens ? subdoelEntity.ontwikkelwens.id : ''}</dd>
          <dt>Aanbod</dt>
          <dd>
            {subdoelEntity.aanbods
              ? subdoelEntity.aanbods.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {subdoelEntity.aanbods && i === subdoelEntity.aanbods.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/subdoel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subdoel/${subdoelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubdoelDetail;
